package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.CachedGauge;
import com.codahale.metrics.annotation.Timed;
import io.swagger.models.auth.In;
import org.mlccc.cm.domain.Authority;
import org.mlccc.cm.domain.Invoice;
import org.mlccc.cm.domain.Registration;
import org.mlccc.cm.domain.User;
import org.mlccc.cm.security.AuthoritiesConstants;
import org.mlccc.cm.service.InvoiceService;
import org.mlccc.cm.service.UserService;
import org.mlccc.cm.service.dto.InvoiceDTO;
import org.mlccc.cm.service.dto.RegistrationDTO;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;

/**
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoice";

    private final InvoiceService invoiceService;

    private final UserService userService;

    public InvoiceResource(InvoiceService invoiceService, UserService userService) {
        this.invoiceService = invoiceService;
        this.userService = userService;
    }

    /**
     * POST  /invoices : Create a new invoice.
     *
     * @param invoice the invoice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoice, or with status 400 (Bad Request) if the invoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoices")
    @Timed
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoice);
        if (invoice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new invoice cannot already have an ID")).body(null);
        }
        Invoice result = invoiceService.save(invoice);
        return ResponseEntity.created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoices : Updates an existing invoice.
     *
     * @param invoice the invoice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoice,
     * or with status 400 (Bad Request) if the invoice is not valid,
     * or with status 500 (Internal Server Error) if the invoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoices")
    @Timed
    public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}", invoice);
        if (invoice.getId() == null) {
            return createInvoice(invoice);
        }
        Invoice result = invoiceService.save(invoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoices : get all the invoices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invoices in body
     */
    @GetMapping("/invoices")
    @Timed
    public List<InvoiceDTO> getAllInvoices() {
        log.debug("REST request to get all Invoices");
        User loginUser = userService.getUserWithAuthorities();
        Set<Authority> authorities = loginUser.getAuthorities();
        Boolean allInvoices = false;
        for(Authority auth : authorities){
            if(auth.getName().equals(AuthoritiesConstants.ADMIN)){
                allInvoices = true;
                break;
            }
        }

        List<Invoice> invoices;
        if(allInvoices) {
            invoices = invoiceService.findAllInvoices();
        } else {
            invoices = invoiceService.findUnpaidByUserId(loginUser.getId());
        }

        List<InvoiceDTO> returnedInvoices = new ArrayList<>();

        for(Invoice invoice : invoices){
            InvoiceDTO invoiceDTO = new InvoiceDTO(invoice);
            returnedInvoices.add(invoiceDTO);
            if(invoice.getRegistrations().size() > 1){
                invoiceDTO.setMultiClassDiscount(-30.00);
            }
            Date now = Calendar.getInstance().getTime();
            if(now.before(new Date())){
                invoiceDTO.setEarlyBirdDiscount(-0.05);
            }
            for(RegistrationDTO registrationDTO : invoiceDTO.getRegistrations()){
                invoiceDTO.setRegistrationFee(registrationDTO.getRegistrationFee());
                invoiceDTO.setTotal(invoiceDTO.getTotal() + registrationDTO.getTuition());
            }
            invoiceDTO.setTotal(invoiceDTO.getTotal()+invoiceDTO.getMultiClassDiscount());
        }
        return returnedInvoices;
    }

    /**
     * GET  /invoices/:id : get the "id" invoice.
     *
     * @param id the id of the invoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoice, or with status 404 (Not Found)
     */
    @GetMapping("/invoices/{id}")
    @Timed
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Invoice invoice = invoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invoice));
    }

    /**
     * DELETE  /invoices/:id : delete the "id" invoice.
     *
     * @param id the id of the invoice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoices/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
