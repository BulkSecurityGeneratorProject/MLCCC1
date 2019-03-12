package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.DiscountCode;

import org.mlccc.cm.repository.DiscountCodeRepository;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DiscountCode.
 */
@RestController
@RequestMapping("/api")
public class DiscountCodeResource {

    private final Logger log = LoggerFactory.getLogger(DiscountCodeResource.class);

    private static final String ENTITY_NAME = "discountCode";

    private final DiscountCodeRepository discountCodeRepository;

    public DiscountCodeResource(DiscountCodeRepository discountCodeRepository) {
        this.discountCodeRepository = discountCodeRepository;
    }

    /**
     * POST  /discount-codes : Create a new discountCode.
     *
     * @param discountCode the discountCode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discountCode, or with status 400 (Bad Request) if the discountCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/discount-codes")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DiscountCode> createDiscountCode(@Valid @RequestBody DiscountCode discountCode) throws URISyntaxException {
        log.debug("REST request to save DiscountCode : {}", discountCode);
        if (discountCode.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new discountCode cannot already have an ID")).body(null);
        }
        DiscountCode result = discountCodeRepository.save(discountCode);
        return ResponseEntity.created(new URI("/api/discount-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discount-codes : Updates an existing discountCode.
     *
     * @param discountCode the discountCode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discountCode,
     * or with status 400 (Bad Request) if the discountCode is not valid,
     * or with status 500 (Internal Server Error) if the discountCode couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/discount-codes")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DiscountCode> updateDiscountCode(@Valid @RequestBody DiscountCode discountCode) throws URISyntaxException {
        log.debug("REST request to update DiscountCode : {}", discountCode);
        if (discountCode.getId() == null) {
            return createDiscountCode(discountCode);
        }
        DiscountCode result = discountCodeRepository.save(discountCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discountCode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discount-codes : get all the discountCodes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discountCodes in body
     */
    @GetMapping("/discount-codes")
    @Timed
    public List<DiscountCode> getAllDiscountCodes() {
        log.debug("REST request to get all DiscountCodes");
        return discountCodeRepository.findAll();
    }

    /**
     * GET  /discount-codes/:id : get the "id" discountCode.
     *
     * @param id the id of the discountCode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discountCode, or with status 404 (Not Found)
     */
    @GetMapping("/discount-codes/{id}")
    @Timed
    public ResponseEntity<DiscountCode> getDiscountCode(@PathVariable Long id) {
        log.debug("REST request to get DiscountCode : {}", id);
        DiscountCode discountCode = discountCodeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(discountCode));
    }

    /**
     * DELETE  /discount-codes/:id : delete the "id" discountCode.
     *
     * @param id the id of the discountCode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/discount-codes/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteDiscountCode(@PathVariable Long id) {
        log.debug("REST request to delete DiscountCode : {}", id);
        discountCodeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
