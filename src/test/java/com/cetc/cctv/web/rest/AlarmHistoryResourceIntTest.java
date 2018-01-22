package com.cetc.cctv.web.rest;

import com.cetc.cctv.CtecApp;

import com.cetc.cctv.domain.AlarmHistory;
import com.cetc.cctv.repository.AlarmHistoryRepository;
import com.cetc.cctv.repository.search.AlarmHistorySearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.cetc.cctv.web.rest.TestUtil.sameInstant;
import static com.cetc.cctv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cetc.cctv.domain.enumeration.AlarmType;
/**
 * Test class for the AlarmHistoryResource REST controller.
 *
 * @see AlarmHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CtecApp.class)
public class AlarmHistoryResourceIntTest {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final AlarmType DEFAULT_ALARM_TYPE = AlarmType.FAST_RUN_WARN;
    private static final AlarmType UPDATED_ALARM_TYPE = AlarmType.PEOPLE_COUNT_LIMIT_WARN;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AlarmHistoryRepository alarmHistoryRepository;

    @Autowired
    private AlarmHistorySearchRepository alarmHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAlarmHistoryMockMvc;

    private AlarmHistory alarmHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlarmHistoryResource alarmHistoryResource = new AlarmHistoryResource(alarmHistoryRepository, alarmHistorySearchRepository);
        this.restAlarmHistoryMockMvc = MockMvcBuilders.standaloneSetup(alarmHistoryResource)
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
    public static AlarmHistory createEntity(EntityManager em) {
        AlarmHistory alarmHistory = new AlarmHistory()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .alarmType(DEFAULT_ALARM_TYPE)
            .time(DEFAULT_TIME);
        return alarmHistory;
    }

    @Before
    public void initTest() {
        alarmHistorySearchRepository.deleteAll();
        alarmHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlarmHistory() throws Exception {
        int databaseSizeBeforeCreate = alarmHistoryRepository.findAll().size();

        // Create the AlarmHistory
        restAlarmHistoryMockMvc.perform(post("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmHistory)))
            .andExpect(status().isCreated());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        AlarmHistory testAlarmHistory = alarmHistoryList.get(alarmHistoryList.size() - 1);
        assertThat(testAlarmHistory.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAlarmHistory.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testAlarmHistory.getAlarmType()).isEqualTo(DEFAULT_ALARM_TYPE);
        assertThat(testAlarmHistory.getTime()).isEqualTo(DEFAULT_TIME);

        // Validate the AlarmHistory in Elasticsearch
        AlarmHistory alarmHistoryEs = alarmHistorySearchRepository.findOne(testAlarmHistory.getId());
        assertThat(testAlarmHistory.getTime()).isEqualTo(testAlarmHistory.getTime());
        assertThat(alarmHistoryEs).isEqualToIgnoringGivenFields(testAlarmHistory, "time");
    }

    @Test
    @Transactional
    public void createAlarmHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alarmHistoryRepository.findAll().size();

        // Create the AlarmHistory with an existing ID
        alarmHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmHistoryMockMvc.perform(post("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmHistory)))
            .andExpect(status().isBadRequest());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAlarmHistories() throws Exception {
        // Initialize the database
        alarmHistoryRepository.saveAndFlush(alarmHistory);

        // Get all the alarmHistoryList
        restAlarmHistoryMockMvc.perform(get("/api/alarm-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].alarmType").value(hasItem(DEFAULT_ALARM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }

    @Test
    @Transactional
    public void getAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.saveAndFlush(alarmHistory);

        // Get the alarmHistory
        restAlarmHistoryMockMvc.perform(get("/api/alarm-histories/{id}", alarmHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alarmHistory.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.alarmType").value(DEFAULT_ALARM_TYPE.toString()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingAlarmHistory() throws Exception {
        // Get the alarmHistory
        restAlarmHistoryMockMvc.perform(get("/api/alarm-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.saveAndFlush(alarmHistory);
        alarmHistorySearchRepository.save(alarmHistory);
        int databaseSizeBeforeUpdate = alarmHistoryRepository.findAll().size();

        // Update the alarmHistory
        AlarmHistory updatedAlarmHistory = alarmHistoryRepository.findOne(alarmHistory.getId());
        // Disconnect from session so that the updates on updatedAlarmHistory are not directly saved in db
        em.detach(updatedAlarmHistory);
        updatedAlarmHistory
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .alarmType(UPDATED_ALARM_TYPE)
            .time(UPDATED_TIME);

        restAlarmHistoryMockMvc.perform(put("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlarmHistory)))
            .andExpect(status().isOk());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeUpdate);
        AlarmHistory testAlarmHistory = alarmHistoryList.get(alarmHistoryList.size() - 1);
        assertThat(testAlarmHistory.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAlarmHistory.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testAlarmHistory.getAlarmType()).isEqualTo(UPDATED_ALARM_TYPE);
        assertThat(testAlarmHistory.getTime()).isEqualTo(UPDATED_TIME);

        // Validate the AlarmHistory in Elasticsearch
        AlarmHistory alarmHistoryEs = alarmHistorySearchRepository.findOne(testAlarmHistory.getId());
        assertThat(testAlarmHistory.getTime()).isEqualTo(testAlarmHistory.getTime());
        assertThat(alarmHistoryEs).isEqualToIgnoringGivenFields(testAlarmHistory, "time");
    }

    @Test
    @Transactional
    public void updateNonExistingAlarmHistory() throws Exception {
        int databaseSizeBeforeUpdate = alarmHistoryRepository.findAll().size();

        // Create the AlarmHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAlarmHistoryMockMvc.perform(put("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmHistory)))
            .andExpect(status().isCreated());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.saveAndFlush(alarmHistory);
        alarmHistorySearchRepository.save(alarmHistory);
        int databaseSizeBeforeDelete = alarmHistoryRepository.findAll().size();

        // Get the alarmHistory
        restAlarmHistoryMockMvc.perform(delete("/api/alarm-histories/{id}", alarmHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean alarmHistoryExistsInEs = alarmHistorySearchRepository.exists(alarmHistory.getId());
        assertThat(alarmHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.saveAndFlush(alarmHistory);
        alarmHistorySearchRepository.save(alarmHistory);

        // Search the alarmHistory
        restAlarmHistoryMockMvc.perform(get("/api/_search/alarm-histories?query=id:" + alarmHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].alarmType").value(hasItem(DEFAULT_ALARM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmHistory.class);
        AlarmHistory alarmHistory1 = new AlarmHistory();
        alarmHistory1.setId(1L);
        AlarmHistory alarmHistory2 = new AlarmHistory();
        alarmHistory2.setId(alarmHistory1.getId());
        assertThat(alarmHistory1).isEqualTo(alarmHistory2);
        alarmHistory2.setId(2L);
        assertThat(alarmHistory1).isNotEqualTo(alarmHistory2);
        alarmHistory1.setId(null);
        assertThat(alarmHistory1).isNotEqualTo(alarmHistory2);
    }
}
