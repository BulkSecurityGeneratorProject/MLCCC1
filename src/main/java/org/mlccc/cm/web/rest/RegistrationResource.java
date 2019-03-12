package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.models.auth.In;
import org.mlccc.cm.config.Constants;
import org.mlccc.cm.domain.*;
import org.mlccc.cm.security.AuthoritiesConstants;
import org.mlccc.cm.service.*;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Registration.
 */
@RestController
@RequestMapping("/api")
public class RegistrationResource {

    private final Logger log = LoggerFactory.getLogger(RegistrationResource.class);

    private static final String ENTITY_NAME = "registration";

    private final RegistrationService registrationService;

    private final UserService userService;

    private final StudentService studentService;

    private final InvoiceService invoiceService;

    private final MlcClassService classService;

    private final ClassStatusService classStatusService;

    public RegistrationResource(RegistrationService registrationService, UserService userService, StudentService studentService,
                                InvoiceService invoiceService, MlcClassService classService, ClassStatusService classStatusService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.studentService = studentService;
        this.invoiceService = invoiceService;
        this.classService = classService;
        this.classStatusService = classStatusService;
    }

    /**
     * POST  /registrations : Create a new registration.
     *
     * @param registration the registration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registration, or with status 400 (Bad Request) if the registration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/registrations")
    @Timed
    public ResponseEntity<Registration> createRegistration(@RequestBody Registration registration) throws URISyntaxException {
        log.debug("REST request to save Registration : {}", registration);
        if (registration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new registration cannot already have an ID")).body(null);
        }
        if(registration.getMlcClass() == null || registration.getMlcClass().getId() == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idnotfound", "A registration must have a class ID")).body(null);
        }

        // check if the class is full
        MlcClass mlcClass = classService.findOne(registration.getMlcClass().getId());
        if(mlcClass.getStatus() != null &&
                (mlcClass.getStatus().getStatus().equals(Constants.FULL_STATUS) || mlcClass.getStatus().getStatus().equals(Constants.CLOSED_STATUS))){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notpossible", "The class is full")).body(null);
        }

        List<Registration> existingRegistrations = registrationService.findAllWithStudentIdClassId(registration.getStudent().getId(),
            registration.getMlcClass().getId());

        if(!existingRegistrations.isEmpty()){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "registrationExists", "The student already registered this class")).body(null);
        }

        Long numOfRegistrations = registrationService.findNumberOfRegistrationWithClassId(mlcClass.getId());

        Student student = studentService.findByIdAndFetchEager(registration.getStudent().getId());
        Set<User> associatedAccounts =student.getAssociatedAccounts();
        User invoicedUser = associatedAccounts.iterator().next();
        List<Invoice> invoices = invoiceService.findUnpaidByUserId(invoicedUser.getId());
        Invoice invoice = new Invoice();
        if(invoices == null || invoices.isEmpty()){
            invoice.setDescription("new registration invoice");
            invoice.setInvoiceDate(LocalDate.now());
            invoice.setStatus("UNPAID");
            invoice.setUser(invoicedUser);
            invoiceService.save(invoice);
        }else {
            invoice = invoices.get(0);
            invoice.setDescription("add new registration to existing invoice");
        }

        registration.setInvoice(invoice);
        registration.setCreateDate(LocalDate.now());
        registration.setStatus(Constants.PENDING_STATUS);
        Registration result = registrationService.save(registration);
        if(result != null){
            if(mlcClass.getSize() != null) {
                if ((numOfRegistrations.doubleValue()+1)/mlcClass.getSize().doubleValue() >= 0.8){
                    mlcClass.setStatus(classStatusService.findByName(Constants.ALMOST_FULL_STATUS));
                    classService.save(mlcClass);
                } else if(numOfRegistrations.doubleValue()+1 >= mlcClass.getSize().doubleValue()){
                    mlcClass.setStatus(classStatusService.findByName(Constants.FULL_STATUS));
                    classService.save(mlcClass);
                }
            }
            invoiceService.save(invoice);
        }

        return ResponseEntity.created(new URI("/api/registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registrations : Updates an existing registration.
     *
     * @param registration the registration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registration,
     * or with status 400 (Bad Request) if the registration is not valid,
     * or with status 500 (Internal Server Error) if the registration couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/registrations")
    @Timed
    public ResponseEntity<Registration> updateRegistration(@RequestBody Registration registration) throws URISyntaxException {
        log.debug("REST request to update Registration : {}", registration);
        if (registration.getId() == null) {
            registration.setCreateDate(LocalDate.now());
            return createRegistration(registration);
        }
        registration.setModifyDate(LocalDate.now());
        Registration result = registrationService.save(registration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, registration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registrations : get all the registrations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of registrations in body
     */
    @GetMapping("/registrations")
    @Timed
    public ResponseEntity<List<Registration>> getAllRegistrations(@ApiParam Pageable pageable, @ApiParam String searchTerm) {
        log.debug("REST request to get a page of Registrations with searchTerm: {}", searchTerm);
        User loginUser = userService.getUserWithAuthorities();
        Set<Authority> authorities = loginUser.getAuthorities();
        Boolean allRegistration = false;
        Boolean isTeacher = false;
        for(Authority auth : authorities){
            if(auth.getName().equals(AuthoritiesConstants.ADMIN)){
                allRegistration = true;
            } else if(auth.getName().equals(AuthoritiesConstants.TEACHER)){
                isTeacher = true;
            }
        }

        Page<Registration> page = null;
        if(allRegistration){
            page = registrationService.findAll(pageable);
        } else {
            if(searchTerm.equalsIgnoreCase("studentsInClass") && isTeacher){
                page = registrationService.findAllWithTeacherUserId(pageable, loginUser.getId());
            } else {
                page = registrationService.findAllWithAssociatedUserId(pageable, loginUser.getId());
            }
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/registrations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /registrations/:id : get the "id" registration.
     *
     * @param id the id of the registration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registration, or with status 404 (Not Found)
     */
    @GetMapping("/registrations/{id}")
    @Timed
    public ResponseEntity<Registration> getRegistration(@PathVariable Long id) {
        log.debug("REST request to get Registration : {}", id);
        Registration registration = registrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(registration));
    }

    /**
     * DELETE  /registrations/:id : delete the "id" registration.
     *
     * @param id the id of the registration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/registrations/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        log.debug("REST request to delete Registration : {}", id);
        registrationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
