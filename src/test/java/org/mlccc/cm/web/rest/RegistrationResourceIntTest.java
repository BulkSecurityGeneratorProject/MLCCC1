package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.ClassStatus;
import org.mlccc.cm.domain.Registration;
import org.mlccc.cm.repository.RegistrationRepository;
import org.mlccc.cm.service.*;
import org.mlccc.cm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RegistrationResource REST controller.
 *
 * @see RegistrationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class RegistrationResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistrationMockMvc;

    private Registration registration;

    private UserService userService;

    private StudentService studentService;

    private InvoiceService invoiceService;

    private MlcClassService mlcClassService;

    private ClassStatusService classStatusService;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegistrationResource registrationResource = new RegistrationResource(registrationService, userService,
                studentService, invoiceService, mlcClassService, classStatusService);
        this.restRegistrationMockMvc = MockMvcBuilders.standaloneSetup(registrationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registration createEntity(EntityManager em) {
        Registration registration = new Registration()
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return registration;
    }

    @Before
    public void initTest() {
        registration = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistration() throws Exception {
        int databaseSizeBeforeCreate = registrationRepository.findAll().size();

        // Create the Registration
        restRegistrationMockMvc.perform(post("/api/registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registration)))
            .andExpect(status().isCreated());

        // Validate the Registration in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeCreate + 1);
        Registration testRegistration = registrationList.get(registrationList.size() - 1);
        assertThat(testRegistration.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRegistration.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testRegistration.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createRegistrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registrationRepository.findAll().size();

        // Create the Registration with an existing ID
        registration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistrationMockMvc.perform(post("/api/registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registration)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegistrations() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        // Get all the registrationList
        restRegistrationMockMvc.perform(get("/api/registrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registration.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRegistration() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        // Get the registration
        restRegistrationMockMvc.perform(get("/api/registrations/{id}", registration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registration.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistration() throws Exception {
        // Get the registration
        restRegistrationMockMvc.perform(get("/api/registrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistration() throws Exception {
        // Initialize the database
        registrationService.save(registration);

        int databaseSizeBeforeUpdate = registrationRepository.findAll().size();

        // Update the registration
        Registration updatedRegistration = registrationRepository.findOne(registration.getId());
        updatedRegistration
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);

        restRegistrationMockMvc.perform(put("/api/registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistration)))
            .andExpect(status().isOk());

        // Validate the Registration in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeUpdate);
        Registration testRegistration = registrationList.get(registrationList.size() - 1);
        assertThat(testRegistration.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRegistration.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testRegistration.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistration() throws Exception {
        int databaseSizeBeforeUpdate = registrationRepository.findAll().size();

        // Create the Registration

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegistrationMockMvc.perform(put("/api/registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registration)))
            .andExpect(status().isCreated());

        // Validate the Registration in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegistration() throws Exception {
        // Initialize the database
        registrationService.save(registration);

        int databaseSizeBeforeDelete = registrationRepository.findAll().size();

        // Get the registration
        restRegistrationMockMvc.perform(delete("/api/registrations/{id}", registration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registration.class);
        Registration registration1 = new Registration();
        registration1.setId(1L);
        Registration registration2 = new Registration();
        registration2.setId(registration1.getId());
        assertThat(registration1).isEqualTo(registration2);
        registration2.setId(2L);
        assertThat(registration1).isNotEqualTo(registration2);
        registration1.setId(null);
        assertThat(registration1).isNotEqualTo(registration2);
    }
}
