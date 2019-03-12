package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.SchoolDistrict;

import org.mlccc.cm.repository.SchoolDistrictRepository;
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
 * REST controller for managing SchoolDistrict.
 */
@RestController
@RequestMapping("/api")
public class SchoolDistrictResource {

    private final Logger log = LoggerFactory.getLogger(SchoolDistrictResource.class);

    private static final String ENTITY_NAME = "schoolDistrict";

    private final SchoolDistrictRepository schoolDistrictRepository;

    public SchoolDistrictResource(SchoolDistrictRepository schoolDistrictRepository) {
        this.schoolDistrictRepository = schoolDistrictRepository;
    }

    /**
     * POST  /school-districts : Create a new schoolDistrict.
     *
     * @param schoolDistrict the schoolDistrict to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schoolDistrict, or with status 400 (Bad Request) if the schoolDistrict has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/school-districts")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<SchoolDistrict> createSchoolDistrict(@RequestBody SchoolDistrict schoolDistrict) throws URISyntaxException {
        log.debug("REST request to save SchoolDistrict : {}", schoolDistrict);
        if (schoolDistrict.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new schoolDistrict cannot already have an ID")).body(null);
        }
        SchoolDistrict result = schoolDistrictRepository.save(schoolDistrict);
        return ResponseEntity.created(new URI("/api/school-districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /school-districts : Updates an existing schoolDistrict.
     *
     * @param schoolDistrict the schoolDistrict to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schoolDistrict,
     * or with status 400 (Bad Request) if the schoolDistrict is not valid,
     * or with status 500 (Internal Server Error) if the schoolDistrict couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/school-districts")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<SchoolDistrict> updateSchoolDistrict(@RequestBody SchoolDistrict schoolDistrict) throws URISyntaxException {
        log.debug("REST request to update SchoolDistrict : {}", schoolDistrict);
        if (schoolDistrict.getId() == null) {
            return createSchoolDistrict(schoolDistrict);
        }
        SchoolDistrict result = schoolDistrictRepository.save(schoolDistrict);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schoolDistrict.getId().toString()))
            .body(result);
    }

    /**
     * GET  /school-districts : get all the schoolDistricts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of schoolDistricts in body
     */
    @GetMapping("/school-districts")
    @Timed
    public List<SchoolDistrict> getAllSchoolDistricts() {
        log.debug("REST request to get all SchoolDistricts");
        return schoolDistrictRepository.findAll();
    }

    /**
     * GET  /school-districts/:id : get the "id" schoolDistrict.
     *
     * @param id the id of the schoolDistrict to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schoolDistrict, or with status 404 (Not Found)
     */
    @GetMapping("/school-districts/{id}")
    @Timed
    public ResponseEntity<SchoolDistrict> getSchoolDistrict(@PathVariable Long id) {
        log.debug("REST request to get SchoolDistrict : {}", id);
        SchoolDistrict schoolDistrict = schoolDistrictRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schoolDistrict));
    }

    /**
     * DELETE  /school-districts/:id : delete the "id" schoolDistrict.
     *
     * @param id the id of the schoolDistrict to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/school-districts/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteSchoolDistrict(@PathVariable Long id) {
        log.debug("REST request to delete SchoolDistrict : {}", id);
        schoolDistrictRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
