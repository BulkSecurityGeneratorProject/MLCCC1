package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.MlcClassCategory;
import org.mlccc.cm.service.MlcClassCategoryService;
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
 * REST controller for managing MlcClassCategory.
 */
@RestController
@RequestMapping("/api")
public class MlcClassCategoryResource {

    private final Logger log = LoggerFactory.getLogger(MlcClassCategoryResource.class);

    private static final String ENTITY_NAME = "mlcClassCategory";

    private final MlcClassCategoryService mlcClassCategoryService;

    public MlcClassCategoryResource(MlcClassCategoryService mlcClassCategoryService) {
        this.mlcClassCategoryService = mlcClassCategoryService;
    }

    /**
     * POST  /mlc-class-categories : Create a new mlcClassCategory.
     *
     * @param mlcClassCategory the mlcClassCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mlcClassCategory, or with status 400 (Bad Request) if the mlcClassCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mlc-class-categories")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MlcClassCategory> createMlcClassCategory(@RequestBody MlcClassCategory mlcClassCategory) throws URISyntaxException {
        log.debug("REST request to save MlcClassCategory : {}", mlcClassCategory);
        if (mlcClassCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mlcClassCategory cannot already have an ID")).body(null);
        }
        MlcClassCategory result = mlcClassCategoryService.save(mlcClassCategory);
        return ResponseEntity.created(new URI("/api/mlc-class-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mlc-class-categories : Updates an existing mlcClassCategory.
     *
     * @param mlcClassCategory the mlcClassCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mlcClassCategory,
     * or with status 400 (Bad Request) if the mlcClassCategory is not valid,
     * or with status 500 (Internal Server Error) if the mlcClassCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mlc-class-categories")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MlcClassCategory> updateMlcClassCategory(@RequestBody MlcClassCategory mlcClassCategory) throws URISyntaxException {
        log.debug("REST request to update MlcClassCategory : {}", mlcClassCategory);
        if (mlcClassCategory.getId() == null) {
            return createMlcClassCategory(mlcClassCategory);
        }
        MlcClassCategory result = mlcClassCategoryService.save(mlcClassCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mlcClassCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mlc-class-categories : get all the mlcClassCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mlcClassCategories in body
     */
    @GetMapping("/mlc-class-categories")
    @Timed
    public List<MlcClassCategory> getAllMlcClassCategories() {
        log.debug("REST request to get all MlcClassCategories");
        return mlcClassCategoryService.findAll();
    }

    /**
     * GET  /mlc-class-categories/:id : get the "id" mlcClassCategory.
     *
     * @param id the id of the mlcClassCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mlcClassCategory, or with status 404 (Not Found)
     */
    @GetMapping("/mlc-class-categories/{id}")
    @Timed
    public ResponseEntity<MlcClassCategory> getMlcClassCategory(@PathVariable Long id) {
        log.debug("REST request to get MlcClassCategory : {}", id);
        MlcClassCategory mlcClassCategory = mlcClassCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mlcClassCategory));
    }

    /**
     * DELETE  /mlc-class-categories/:id : delete the "id" mlcClassCategory.
     *
     * @param id the id of the mlcClassCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mlc-class-categories/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteMlcClassCategory(@PathVariable Long id) {
        log.debug("REST request to delete MlcClassCategory : {}", id);
        mlcClassCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
