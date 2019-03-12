package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.domain.Registration;
import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.service.RegistrationService;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import org.mlccc.cm.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MlcClass.
 */
@RestController
@RequestMapping("/api")
public class MlcClassResource {

    private final Logger log = LoggerFactory.getLogger(MlcClassResource.class);

    private static final String ENTITY_NAME = "mlcClass";

    private final MlcClassService mlcClassService;

    private final RegistrationService registrationService;

    public MlcClassResource(MlcClassService mlcClassService, RegistrationService registrationService) {
        this.mlcClassService = mlcClassService;
        this.registrationService = registrationService;
    }

    /**
     * POST  /mlc-classes : Create a new mlcClass.
     *
     * @param mlcClass the mlcClass to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mlcClass, or with status 400 (Bad Request) if the mlcClass has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mlc-classes")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MlcClass> createMlcClass(@RequestBody MlcClass mlcClass) throws URISyntaxException {
        log.debug("REST request to save MlcClass : {}", mlcClass);
        if (mlcClass.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mlcClass cannot already have an ID")).body(null);
        }
        MlcClass result = mlcClassService.save(mlcClass);
        return ResponseEntity.created(new URI("/api/mlc-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mlc-classes : Updates an existing mlcClass.
     *
     * @param mlcClass the mlcClass to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mlcClass,
     * or with status 400 (Bad Request) if the mlcClass is not valid,
     * or with status 500 (Internal Server Error) if the mlcClass couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mlc-classes")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MlcClass> updateMlcClass(@RequestBody MlcClass mlcClass) throws URISyntaxException {
        log.debug("REST request to update MlcClass : {}", mlcClass);
        if (mlcClass.getId() == null) {
            return createMlcClass(mlcClass);
        }

        /*if(mlcClass.getRegistrations() != null && registrationService.findAllWithClassId(mlcClass.getId()) != null) {
            mlcClass.setRegistrations(new HashSet<Registration>(registrationService.findAllWithClassId(mlcClass.getId())));
        }*/
        MlcClass result = mlcClassService.save(mlcClass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mlcClass.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mlc-classes : get all the mlcClasses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mlcClasses in body
     */
    @GetMapping("/mlc-classes")
    @Timed
    public ResponseEntity<List<MlcClass>> getAllMlcClasses(@ApiParam Pageable pageable, @ApiParam String search, @ApiParam Long category,
                                                           @ApiParam Long teacher) {
        log.debug("REST request to get a page of MlcClasses");
        Page<MlcClass> page = mlcClassService.findAllWithSearchTerm(pageable, search, category, teacher);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mlc-classes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mlc-classes/:id : get the "id" mlcClass.
     *
     * @param id the id of the mlcClass to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mlcClass, or with status 404 (Not Found)
     */
    @GetMapping("/mlc-classes/{id}")
    @Timed
    public ResponseEntity<MlcClass> getMlcClass(@PathVariable Long id) {
        log.debug("REST request to get MlcClass : {}", id);
        MlcClass mlcClass = mlcClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mlcClass));
    }

    /**
     * DELETE  /mlc-classes/:id : delete the "id" mlcClass.
     *
     * @param id the id of the mlcClass to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mlc-classes/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteMlcClass(@PathVariable Long id) {
        log.debug("REST request to delete MlcClass : {}", id);
        mlcClassService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
