package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.AppliedDiscount;
import org.mlccc.cm.service.AppliedDiscountService;
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
 * REST controller for managing AppliedDiscount.
 */
@RestController
@RequestMapping("/api")
public class AppliedDiscountResource {

    private final Logger log = LoggerFactory.getLogger(AppliedDiscountResource.class);

    private static final String ENTITY_NAME = "appliedDiscount";

    private final AppliedDiscountService appliedDiscountService;

    public AppliedDiscountResource(AppliedDiscountService appliedDiscountService) {
        this.appliedDiscountService = appliedDiscountService;
    }

    /**
     * POST  /applied-discounts : Create a new appliedDiscount.
     *
     * @param appliedDiscount the appliedDiscount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appliedDiscount, or with status 400 (Bad Request) if the appliedDiscount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/applied-discounts")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AppliedDiscount> createAppliedDiscount(@RequestBody AppliedDiscount appliedDiscount) throws URISyntaxException {
        log.debug("REST request to save AppliedDiscount : {}", appliedDiscount);
        if (appliedDiscount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new appliedDiscount cannot already have an ID")).body(null);
        }
        AppliedDiscount result = appliedDiscountService.save(appliedDiscount);
        return ResponseEntity.created(new URI("/api/applied-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applied-discounts : Updates an existing appliedDiscount.
     *
     * @param appliedDiscount the appliedDiscount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appliedDiscount,
     * or with status 400 (Bad Request) if the appliedDiscount is not valid,
     * or with status 500 (Internal Server Error) if the appliedDiscount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/applied-discounts")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AppliedDiscount> updateAppliedDiscount(@RequestBody AppliedDiscount appliedDiscount) throws URISyntaxException {
        log.debug("REST request to update AppliedDiscount : {}", appliedDiscount);
        if (appliedDiscount.getId() == null) {
            return createAppliedDiscount(appliedDiscount);
        }
        AppliedDiscount result = appliedDiscountService.save(appliedDiscount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appliedDiscount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applied-discounts : get all the appliedDiscounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of appliedDiscounts in body
     */
    @GetMapping("/applied-discounts")
    @Timed
    public List<AppliedDiscount> getAllAppliedDiscounts() {
        log.debug("REST request to get all AppliedDiscounts");
        return appliedDiscountService.findAll();
    }

    /**
     * GET  /applied-discounts/:id : get the "id" appliedDiscount.
     *
     * @param id the id of the appliedDiscount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appliedDiscount, or with status 404 (Not Found)
     */
    @GetMapping("/applied-discounts/{id}")
    @Timed
    public ResponseEntity<AppliedDiscount> getAppliedDiscount(@PathVariable Long id) {
        log.debug("REST request to get AppliedDiscount : {}", id);
        AppliedDiscount appliedDiscount = appliedDiscountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appliedDiscount));
    }

    /**
     * DELETE  /applied-discounts/:id : delete the "id" appliedDiscount.
     *
     * @param id the id of the appliedDiscount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/applied-discounts/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteAppliedDiscount(@PathVariable Long id) {
        log.debug("REST request to delete AppliedDiscount : {}", id);
        appliedDiscountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
