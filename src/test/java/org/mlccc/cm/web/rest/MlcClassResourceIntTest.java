package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.repository.MlcClassRepository;
import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.service.RegistrationService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MlcClassResource REST controller.
 *
 * @see MlcClassResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class MlcClassResourceIntTest {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_BOOK = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_BOOK = "BBBBBBBBBB";

    private static final Integer DEFAULT_SIZE = 1;
    private static final Integer UPDATED_SIZE = 2;

    private static final Integer DEFAULT_MIN_AGE = 1;
    private static final Integer UPDATED_MIN_AGE = 2;

    private static final Double DEFAULT_TUITION = 1D;
    private static final Double UPDATED_TUITION = 2D;

    private static final Double DEFAULT_REGISTRATION_FEE = 1D;
    private static final Double UPDATED_REGISTRATION_FEE = 2D;

    private static final Integer DEFAULT_SEQ_NUMBER = 1;
    private static final Integer UPDATED_SEQ_NUMBER = 2;

    @Autowired
    private MlcClassRepository mlcClassRepository;

    @Autowired
    private MlcClassService mlcClassService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMlcClassMockMvc;

    private MlcClass mlcClass;

    private RegistrationService registrationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MlcClassResource mlcClassResource = new MlcClassResource(mlcClassService, registrationService);
        this.restMlcClassMockMvc = MockMvcBuilders.standaloneSetup(mlcClassResource)
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
    public static MlcClass createEntity(EntityManager em) {
        MlcClass mlcClass = new MlcClass()
            .className(DEFAULT_CLASS_NAME)
            .textBook(DEFAULT_TEXT_BOOK)
            .size(DEFAULT_SIZE)
            .minAge(DEFAULT_MIN_AGE)
            .tuition(DEFAULT_TUITION)
            .registrationFee(DEFAULT_REGISTRATION_FEE)
            .seqNumber(DEFAULT_SEQ_NUMBER);
        return mlcClass;
    }

    @Before
    public void initTest() {
        mlcClass = createEntity(em);
    }

    @Test
    @Transactional
    public void createMlcClass() throws Exception {
        int databaseSizeBeforeCreate = mlcClassRepository.findAll().size();

        // Create the MlcClass
        restMlcClassMockMvc.perform(post("/api/mlc-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mlcClass)))
            .andExpect(status().isCreated());

        // Validate the MlcClass in the database
        List<MlcClass> mlcClassList = mlcClassRepository.findAll();
        assertThat(mlcClassList).hasSize(databaseSizeBeforeCreate + 1);
        MlcClass testMlcClass = mlcClassList.get(mlcClassList.size() - 1);
        assertThat(testMlcClass.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testMlcClass.getTextBook()).isEqualTo(DEFAULT_TEXT_BOOK);
        assertThat(testMlcClass.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testMlcClass.getMinAge()).isEqualTo(DEFAULT_MIN_AGE);
        assertThat(testMlcClass.getTuition()).isEqualTo(DEFAULT_TUITION);
        assertThat(testMlcClass.getRegistrationFee()).isEqualTo(DEFAULT_REGISTRATION_FEE);
        assertThat(testMlcClass.getSeqNumber()).isEqualTo(DEFAULT_SEQ_NUMBER);
    }

    @Test
    @Transactional
    public void createMlcClassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mlcClassRepository.findAll().size();

        // Create the MlcClass with an existing ID
        mlcClass.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMlcClassMockMvc.perform(post("/api/mlc-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mlcClass)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MlcClass> mlcClassList = mlcClassRepository.findAll();
        assertThat(mlcClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMlcClasses() throws Exception {
        // Initialize the database
        mlcClassRepository.saveAndFlush(mlcClass);

        // Get all the mlcClassList
        restMlcClassMockMvc.perform(get("/api/mlc-classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mlcClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].textBook").value(hasItem(DEFAULT_TEXT_BOOK.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].minAge").value(hasItem(DEFAULT_MIN_AGE)))
            .andExpect(jsonPath("$.[*].tuition").value(hasItem(DEFAULT_TUITION.doubleValue())))
            .andExpect(jsonPath("$.[*].registrationFee").value(hasItem(DEFAULT_REGISTRATION_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].seqNumber").value(hasItem(DEFAULT_SEQ_NUMBER)));
    }

    @Test
    @Transactional
    public void getMlcClass() throws Exception {
        // Initialize the database
        mlcClassRepository.saveAndFlush(mlcClass);

        // Get the mlcClass
        restMlcClassMockMvc.perform(get("/api/mlc-classes/{id}", mlcClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mlcClass.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.textBook").value(DEFAULT_TEXT_BOOK.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.minAge").value(DEFAULT_MIN_AGE))
            .andExpect(jsonPath("$.tuition").value(DEFAULT_TUITION.doubleValue()))
            .andExpect(jsonPath("$.registrationFee").value(DEFAULT_REGISTRATION_FEE.doubleValue()))
            .andExpect(jsonPath("$.seqNumber").value(DEFAULT_SEQ_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingMlcClass() throws Exception {
        // Get the mlcClass
        restMlcClassMockMvc.perform(get("/api/mlc-classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMlcClass() throws Exception {
        // Initialize the database
        mlcClassService.save(mlcClass);

        int databaseSizeBeforeUpdate = mlcClassRepository.findAll().size();

        // Update the mlcClass
        MlcClass updatedMlcClass = mlcClassRepository.findOne(mlcClass.getId());
        updatedMlcClass
            .className(UPDATED_CLASS_NAME)
            .textBook(UPDATED_TEXT_BOOK)
            .size(UPDATED_SIZE)
            .minAge(UPDATED_MIN_AGE)
            .tuition(UPDATED_TUITION)
            .registrationFee(UPDATED_REGISTRATION_FEE)
            .seqNumber(UPDATED_SEQ_NUMBER);

        restMlcClassMockMvc.perform(put("/api/mlc-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMlcClass)))
            .andExpect(status().isOk());

        // Validate the MlcClass in the database
        List<MlcClass> mlcClassList = mlcClassRepository.findAll();
        assertThat(mlcClassList).hasSize(databaseSizeBeforeUpdate);
        MlcClass testMlcClass = mlcClassList.get(mlcClassList.size() - 1);
        assertThat(testMlcClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testMlcClass.getTextBook()).isEqualTo(UPDATED_TEXT_BOOK);
        assertThat(testMlcClass.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testMlcClass.getMinAge()).isEqualTo(UPDATED_MIN_AGE);
        assertThat(testMlcClass.getTuition()).isEqualTo(UPDATED_TUITION);
        assertThat(testMlcClass.getRegistrationFee()).isEqualTo(UPDATED_REGISTRATION_FEE);
        assertThat(testMlcClass.getSeqNumber()).isEqualTo(UPDATED_SEQ_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingMlcClass() throws Exception {
        int databaseSizeBeforeUpdate = mlcClassRepository.findAll().size();

        // Create the MlcClass

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMlcClassMockMvc.perform(put("/api/mlc-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mlcClass)))
            .andExpect(status().isCreated());

        // Validate the MlcClass in the database
        List<MlcClass> mlcClassList = mlcClassRepository.findAll();
        assertThat(mlcClassList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMlcClass() throws Exception {
        // Initialize the database
        mlcClassService.save(mlcClass);

        int databaseSizeBeforeDelete = mlcClassRepository.findAll().size();

        // Get the mlcClass
        restMlcClassMockMvc.perform(delete("/api/mlc-classes/{id}", mlcClass.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MlcClass> mlcClassList = mlcClassRepository.findAll();
        assertThat(mlcClassList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MlcClass.class);
        MlcClass mlcClass1 = new MlcClass();
        mlcClass1.setId(1L);
        MlcClass mlcClass2 = new MlcClass();
        mlcClass2.setId(mlcClass1.getId());
        assertThat(mlcClass1).isEqualTo(mlcClass2);
        mlcClass2.setId(2L);
        assertThat(mlcClass1).isNotEqualTo(mlcClass2);
        mlcClass1.setId(null);
        assertThat(mlcClass1).isNotEqualTo(mlcClass2);
    }
}
