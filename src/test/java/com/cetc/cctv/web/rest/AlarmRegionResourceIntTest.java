package com.cetc.cctv.web.rest;

import com.cetc.cctv.CtecApp;

import com.cetc.cctv.domain.AlarmRegion;
import com.cetc.cctv.repository.AlarmRegionRepository;
import com.cetc.cctv.repository.search.AlarmRegionSearchRepository;
import com.cetc.cctv.web.rest.errors.ExceptionTranslator;

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

import static com.cetc.cctv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AlarmRegionResource REST controller.
 *
 * @see AlarmRegionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CtecApp.class)
public class AlarmRegionResourceIntTest {

    private static final Float DEFAULT_LEFT_UP_X = 1F;
    private static final Float UPDATED_LEFT_UP_X = 2F;

    private static final Float DEFAULT_LEFT_UP_Y = 1F;
    private static final Float UPDATED_LEFT_UP_Y = 2F;

    private static final Float DEFAULT_RIGHT_UP_X = 1F;
    private static final Float UPDATED_RIGHT_UP_X = 2F;

    private static final Float DEFAULT_RIGHT_UP_Y = 1F;
    private static final Float UPDATED_RIGHT_UP_Y = 2F;

    private static final Float DEFAULT_LEFT_DOWN_X = 1F;
    private static final Float UPDATED_LEFT_DOWN_X = 2F;

    private static final Float DEFAULT_LEFT_DOWN_Y = 1F;
    private static final Float UPDATED_LEFT_DOWN_Y = 2F;

    private static final Float DEFAULT_RIGHT_DOWN_X = 1F;
    private static final Float UPDATED_RIGHT_DOWN_X = 2F;

    private static final Float DEFAULT_RIGHT_DOWN_Y = 1F;
    private static final Float UPDATED_RIGHT_DOWN_Y = 2F;

    @Autowired
    private AlarmRegionRepository alarmRegionRepository;

    @Autowired
    private AlarmRegionSearchRepository alarmRegionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAlarmRegionMockMvc;

    private AlarmRegion alarmRegion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlarmRegionResource alarmRegionResource = new AlarmRegionResource(alarmRegionRepository, alarmRegionSearchRepository);
        this.restAlarmRegionMockMvc = MockMvcBuilders.standaloneSetup(alarmRegionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlarmRegion createEntity(EntityManager em) {
        AlarmRegion alarmRegion = new AlarmRegion()
            .leftUpX(DEFAULT_LEFT_UP_X)
            .leftUpY(DEFAULT_LEFT_UP_Y)
            .rightUpX(DEFAULT_RIGHT_UP_X)
            .rightUpY(DEFAULT_RIGHT_UP_Y)
            .leftDownX(DEFAULT_LEFT_DOWN_X)
            .leftDownY(DEFAULT_LEFT_DOWN_Y)
            .rightDownX(DEFAULT_RIGHT_DOWN_X)
            .rightDownY(DEFAULT_RIGHT_DOWN_Y);
        return alarmRegion;
    }

    @Before
    public void initTest() {
        alarmRegionSearchRepository.deleteAll();
        alarmRegion = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlarmRegion() throws Exception {
        int databaseSizeBeforeCreate = alarmRegionRepository.findAll().size();

        // Create the AlarmRegion
        restAlarmRegionMockMvc.perform(post("/api/alarm-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmRegion)))
            .andExpect(status().isCreated());

        // Validate the AlarmRegion in the database
        List<AlarmRegion> alarmRegionList = alarmRegionRepository.findAll();
        assertThat(alarmRegionList).hasSize(databaseSizeBeforeCreate + 1);
        AlarmRegion testAlarmRegion = alarmRegionList.get(alarmRegionList.size() - 1);
        assertThat(testAlarmRegion.getLeftUpX()).isEqualTo(DEFAULT_LEFT_UP_X);
        assertThat(testAlarmRegion.getLeftUpY()).isEqualTo(DEFAULT_LEFT_UP_Y);
        assertThat(testAlarmRegion.getRightUpX()).isEqualTo(DEFAULT_RIGHT_UP_X);
        assertThat(testAlarmRegion.getRightUpY()).isEqualTo(DEFAULT_RIGHT_UP_Y);
        assertThat(testAlarmRegion.getLeftDownX()).isEqualTo(DEFAULT_LEFT_DOWN_X);
        assertThat(testAlarmRegion.getLeftDownY()).isEqualTo(DEFAULT_LEFT_DOWN_Y);
        assertThat(testAlarmRegion.getRightDownX()).isEqualTo(DEFAULT_RIGHT_DOWN_X);
        assertThat(testAlarmRegion.getRightDownY()).isEqualTo(DEFAULT_RIGHT_DOWN_Y);

        // Validate the AlarmRegion in Elasticsearch
        AlarmRegion alarmRegionEs = alarmRegionSearchRepository.findOne(testAlarmRegion.getId());
        assertThat(alarmRegionEs).isEqualToIgnoringGivenFields(testAlarmRegion);
    }

    @Test
    @Transactional
    public void createAlarmRegionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alarmRegionRepository.findAll().size();

        // Create the AlarmRegion with an existing ID
        alarmRegion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmRegionMockMvc.perform(post("/api/alarm-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmRegion)))
            .andExpect(status().isBadRequest());

        // Validate the AlarmRegion in the database
        List<AlarmRegion> alarmRegionList = alarmRegionRepository.findAll();
        assertThat(alarmRegionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAlarmRegions() throws Exception {
        // Initialize the database
        alarmRegionRepository.saveAndFlush(alarmRegion);

        // Get all the alarmRegionList
        restAlarmRegionMockMvc.perform(get("/api/alarm-regions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].leftUpX").value(hasItem(DEFAULT_LEFT_UP_X.doubleValue())))
            .andExpect(jsonPath("$.[*].leftUpY").value(hasItem(DEFAULT_LEFT_UP_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].rightUpX").value(hasItem(DEFAULT_RIGHT_UP_X.doubleValue())))
            .andExpect(jsonPath("$.[*].rightUpY").value(hasItem(DEFAULT_RIGHT_UP_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].leftDownX").value(hasItem(DEFAULT_LEFT_DOWN_X.doubleValue())))
            .andExpect(jsonPath("$.[*].leftDownY").value(hasItem(DEFAULT_LEFT_DOWN_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].rightDownX").value(hasItem(DEFAULT_RIGHT_DOWN_X.doubleValue())))
            .andExpect(jsonPath("$.[*].rightDownY").value(hasItem(DEFAULT_RIGHT_DOWN_Y.doubleValue())));
    }

    @Test
    @Transactional
    public void getAlarmRegion() throws Exception {
        // Initialize the database
        alarmRegionRepository.saveAndFlush(alarmRegion);

        // Get the alarmRegion
        restAlarmRegionMockMvc.perform(get("/api/alarm-regions/{id}", alarmRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alarmRegion.getId().intValue()))
            .andExpect(jsonPath("$.leftUpX").value(DEFAULT_LEFT_UP_X.doubleValue()))
            .andExpect(jsonPath("$.leftUpY").value(DEFAULT_LEFT_UP_Y.doubleValue()))
            .andExpect(jsonPath("$.rightUpX").value(DEFAULT_RIGHT_UP_X.doubleValue()))
            .andExpect(jsonPath("$.rightUpY").value(DEFAULT_RIGHT_UP_Y.doubleValue()))
            .andExpect(jsonPath("$.leftDownX").value(DEFAULT_LEFT_DOWN_X.doubleValue()))
            .andExpect(jsonPath("$.leftDownY").value(DEFAULT_LEFT_DOWN_Y.doubleValue()))
            .andExpect(jsonPath("$.rightDownX").value(DEFAULT_RIGHT_DOWN_X.doubleValue()))
            .andExpect(jsonPath("$.rightDownY").value(DEFAULT_RIGHT_DOWN_Y.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAlarmRegion() throws Exception {
        // Get the alarmRegion
        restAlarmRegionMockMvc.perform(get("/api/alarm-regions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlarmRegion() throws Exception {
        // Initialize the database
        alarmRegionRepository.saveAndFlush(alarmRegion);
        alarmRegionSearchRepository.save(alarmRegion);
        int databaseSizeBeforeUpdate = alarmRegionRepository.findAll().size();

        // Update the alarmRegion
        AlarmRegion updatedAlarmRegion = alarmRegionRepository.findOne(alarmRegion.getId());
        // Disconnect from session so that the updates on updatedAlarmRegion are not directly saved in db
        em.detach(updatedAlarmRegion);
        updatedAlarmRegion
            .leftUpX(UPDATED_LEFT_UP_X)
            .leftUpY(UPDATED_LEFT_UP_Y)
            .rightUpX(UPDATED_RIGHT_UP_X)
            .rightUpY(UPDATED_RIGHT_UP_Y)
            .leftDownX(UPDATED_LEFT_DOWN_X)
            .leftDownY(UPDATED_LEFT_DOWN_Y)
            .rightDownX(UPDATED_RIGHT_DOWN_X)
            .rightDownY(UPDATED_RIGHT_DOWN_Y);

        restAlarmRegionMockMvc.perform(put("/api/alarm-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlarmRegion)))
            .andExpect(status().isOk());

        // Validate the AlarmRegion in the database
        List<AlarmRegion> alarmRegionList = alarmRegionRepository.findAll();
        assertThat(alarmRegionList).hasSize(databaseSizeBeforeUpdate);
        AlarmRegion testAlarmRegion = alarmRegionList.get(alarmRegionList.size() - 1);
        assertThat(testAlarmRegion.getLeftUpX()).isEqualTo(UPDATED_LEFT_UP_X);
        assertThat(testAlarmRegion.getLeftUpY()).isEqualTo(UPDATED_LEFT_UP_Y);
        assertThat(testAlarmRegion.getRightUpX()).isEqualTo(UPDATED_RIGHT_UP_X);
        assertThat(testAlarmRegion.getRightUpY()).isEqualTo(UPDATED_RIGHT_UP_Y);
        assertThat(testAlarmRegion.getLeftDownX()).isEqualTo(UPDATED_LEFT_DOWN_X);
        assertThat(testAlarmRegion.getLeftDownY()).isEqualTo(UPDATED_LEFT_DOWN_Y);
        assertThat(testAlarmRegion.getRightDownX()).isEqualTo(UPDATED_RIGHT_DOWN_X);
        assertThat(testAlarmRegion.getRightDownY()).isEqualTo(UPDATED_RIGHT_DOWN_Y);

        // Validate the AlarmRegion in Elasticsearch
        AlarmRegion alarmRegionEs = alarmRegionSearchRepository.findOne(testAlarmRegion.getId());
        assertThat(alarmRegionEs).isEqualToIgnoringGivenFields(testAlarmRegion);
    }

    @Test
    @Transactional
    public void updateNonExistingAlarmRegion() throws Exception {
        int databaseSizeBeforeUpdate = alarmRegionRepository.findAll().size();

        // Create the AlarmRegion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAlarmRegionMockMvc.perform(put("/api/alarm-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmRegion)))
            .andExpect(status().isCreated());

        // Validate the AlarmRegion in the database
        List<AlarmRegion> alarmRegionList = alarmRegionRepository.findAll();
        assertThat(alarmRegionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAlarmRegion() throws Exception {
        // Initialize the database
        alarmRegionRepository.saveAndFlush(alarmRegion);
        alarmRegionSearchRepository.save(alarmRegion);
        int databaseSizeBeforeDelete = alarmRegionRepository.findAll().size();

        // Get the alarmRegion
        restAlarmRegionMockMvc.perform(delete("/api/alarm-regions/{id}", alarmRegion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean alarmRegionExistsInEs = alarmRegionSearchRepository.exists(alarmRegion.getId());
        assertThat(alarmRegionExistsInEs).isFalse();

        // Validate the database is empty
        List<AlarmRegion> alarmRegionList = alarmRegionRepository.findAll();
        assertThat(alarmRegionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAlarmRegion() throws Exception {
        // Initialize the database
        alarmRegionRepository.saveAndFlush(alarmRegion);
        alarmRegionSearchRepository.save(alarmRegion);

        // Search the alarmRegion
        restAlarmRegionMockMvc.perform(get("/api/_search/alarm-regions?query=id:" + alarmRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].leftUpX").value(hasItem(DEFAULT_LEFT_UP_X.doubleValue())))
            .andExpect(jsonPath("$.[*].leftUpY").value(hasItem(DEFAULT_LEFT_UP_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].rightUpX").value(hasItem(DEFAULT_RIGHT_UP_X.doubleValue())))
            .andExpect(jsonPath("$.[*].rightUpY").value(hasItem(DEFAULT_RIGHT_UP_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].leftDownX").value(hasItem(DEFAULT_LEFT_DOWN_X.doubleValue())))
            .andExpect(jsonPath("$.[*].leftDownY").value(hasItem(DEFAULT_LEFT_DOWN_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].rightDownX").value(hasItem(DEFAULT_RIGHT_DOWN_X.doubleValue())))
            .andExpect(jsonPath("$.[*].rightDownY").value(hasItem(DEFAULT_RIGHT_DOWN_Y.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmRegion.class);
        AlarmRegion alarmRegion1 = new AlarmRegion();
        alarmRegion1.setId(1L);
        AlarmRegion alarmRegion2 = new AlarmRegion();
        alarmRegion2.setId(alarmRegion1.getId());
        assertThat(alarmRegion1).isEqualTo(alarmRegion2);
        alarmRegion2.setId(2L);
        assertThat(alarmRegion1).isNotEqualTo(alarmRegion2);
        alarmRegion1.setId(null);
        assertThat(alarmRegion1).isNotEqualTo(alarmRegion2);
    }
}
