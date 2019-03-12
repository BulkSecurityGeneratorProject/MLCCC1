package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.ClassTime;
import org.mlccc.cm.service.ClassTimeService;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ClassTime.
 */
@RestController
@RequestMapping("/api")
public class ClassTimeResource {

    private final Logger log = LoggerFactory.getLogger(ClassTimeResource.class);

    private static final String ENTITY_NAME = "classTime";

    private final ClassTimeService classTimeService;

    public ClassTimeResource(ClassTimeService classTimeService) {
        this.classTimeService = classTimeService;
    }

    /**
     * POST  /class-times : Create a new classTime.
     *
     * @param classTime the classTime to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classTime, or with status 400 (Bad Request) if the classTime has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/class-times")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ClassTime> createClassTime(@RequestBody ClassTime classTime) throws URISyntaxException {
        log.debug("REST request to save ClassTime : {}", classTime);
        if (classTime.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new classTime cannot already have an ID")).body(null);
        }
        ClassTime result = classTimeService.save(classTime);
        return ResponseEntity.created(new URI("/api/class-times/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /class-times : Updates an existing classTime.
     *
     * @param classTime the classTime to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classTime,
     * or with status 400 (Bad Request) if the classTime is not valid,
     * or with status 500 (Internal Server Error) if the classTime couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/class-times")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ClassTime> updateClassTime(@RequestBody ClassTime classTime) throws URISyntaxException {
        log.debug("REST request to update ClassTime : {}", classTime);
        if (classTime.getId() == null) {
            return createClassTime(classTime);
        }
        ClassTime result = classTimeService.save(classTime);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classTime.getId().toString()))
            .body(result);
    }

    /**
     * GET  /class-times : get all the classTimes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of classTimes in body
     */
    @GetMapping("/class-times")
    @Timed
    public List<ClassTime> getAllClassTimes() {
        log.debug("REST request to get all ClassTimes");
        return classTimeService.findAll();
    }

    /**
     * GET  /class-times/:id : get the "id" classTime.
     *
     * @param id the id of the classTime to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classTime, or with status 404 (Not Found)
     */
    @GetMapping("/class-times/{id}")
    @Timed
    public ResponseEntity<ClassTime> getClassTime(@PathVariable Long id) {
        log.debug("REST request to get ClassTime : {}", id);
        ClassTime classTime = classTimeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(classTime));
    }

    /**
     * DELETE  /class-times/:id : delete the "id" classTime.
     *
     * @param id the id of the classTime to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/class-times/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteClassTime(@PathVariable Long id) {
        log.debug("REST request to delete ClassTime : {}", id);
        classTimeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
