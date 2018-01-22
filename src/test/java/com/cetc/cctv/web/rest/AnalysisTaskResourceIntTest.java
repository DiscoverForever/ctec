package com.cetc.cctv.web.rest;

import com.cetc.cctv.CtecApp;

import com.cetc.cctv.domain.AnalysisTask;
import com.cetc.cctv.repository.AnalysisTaskRepository;
import com.cetc.cctv.repository.search.AnalysisTaskSearchRepository;
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

import com.cetc.cctv.domain.enumeration.AnalysisType;
import com.cetc.cctv.domain.enumeration.Priority;
import com.cetc.cctv.domain.enumeration.AnalysisStatus;
import com.cetc.cctv.domain.enumeration.VideoType;
/**
 * Test class for the AnalysisTaskResource REST controller.
 *
 * @see AnalysisTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CtecApp.class)
public class AnalysisTaskResourceIntTest {

    private static final AnalysisType DEFAULT_ANALYSIS_TYPE = AnalysisType.BEHAVIOR;
    private static final AnalysisType UPDATED_ANALYSIS_TYPE = AnalysisType.STRUCTURED;

    private static final Priority DEFAULT_PRIORITY = Priority.HIGHT;
    private static final Priority UPDATED_PRIORITY = Priority.MIDDLE;

    private static final AnalysisStatus DEFAULT_ANALYSIS_STATUS = AnalysisStatus.ON_ANALYSIS;
    private static final AnalysisStatus UPDATED_ANALYSIS_STATUS = AnalysisStatus.ON_PAUSE;

    private static final VideoType DEFAULT_VIDEO_TYPE = VideoType.LOCAL;
    private static final VideoType UPDATED_VIDEO_TYPE = VideoType.REALTIME;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXECUTION_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXECUTION_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AnalysisTaskRepository analysisTaskRepository;

    @Autowired
    private AnalysisTaskSearchRepository analysisTaskSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnalysisTaskMockMvc;

    private AnalysisTask analysisTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnalysisTaskResource analysisTaskResource = new AnalysisTaskResource(analysisTaskRepository, analysisTaskSearchRepository);
        this.restAnalysisTaskMockMvc = MockMvcBuilders.standaloneSetup(analysisTaskResource)
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
    public static AnalysisTask createEntity(EntityManager em) {
        AnalysisTask analysisTask = new AnalysisTask()
            .analysisType(DEFAULT_ANALYSIS_TYPE)
            .priority(DEFAULT_PRIORITY)
            .analysisStatus(DEFAULT_ANALYSIS_STATUS)
            .videoType(DEFAULT_VIDEO_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .executionAt(DEFAULT_EXECUTION_AT);
        return analysisTask;
    }

    @Before
    public void initTest() {
        analysisTaskSearchRepository.deleteAll();
        analysisTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalysisTask() throws Exception {
        int databaseSizeBeforeCreate = analysisTaskRepository.findAll().size();

        // Create the AnalysisTask
        restAnalysisTaskMockMvc.perform(post("/api/analysis-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisTask)))
            .andExpect(status().isCreated());

        // Validate the AnalysisTask in the database
        List<AnalysisTask> analysisTaskList = analysisTaskRepository.findAll();
        assertThat(analysisTaskList).hasSize(databaseSizeBeforeCreate + 1);
        AnalysisTask testAnalysisTask = analysisTaskList.get(analysisTaskList.size() - 1);
        assertThat(testAnalysisTask.getAnalysisType()).isEqualTo(DEFAULT_ANALYSIS_TYPE);
        assertThat(testAnalysisTask.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testAnalysisTask.getAnalysisStatus()).isEqualTo(DEFAULT_ANALYSIS_STATUS);
        assertThat(testAnalysisTask.getVideoType()).isEqualTo(DEFAULT_VIDEO_TYPE);
        assertThat(testAnalysisTask.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAnalysisTask.getExecutionAt()).isEqualTo(DEFAULT_EXECUTION_AT);

        // Validate the AnalysisTask in Elasticsearch
        AnalysisTask analysisTaskEs = analysisTaskSearchRepository.findOne(testAnalysisTask.getId());
        assertThat(testAnalysisTask.getCreatedAt()).isEqualTo(testAnalysisTask.getCreatedAt());
        assertThat(testAnalysisTask.getExecutionAt()).isEqualTo(testAnalysisTask.getExecutionAt());
        assertThat(analysisTaskEs).isEqualToIgnoringGivenFields(testAnalysisTask, "createdAt", "executionAt");
    }

    @Test
    @Transactional
    public void createAnalysisTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analysisTaskRepository.findAll().size();

        // Create the AnalysisTask with an existing ID
        analysisTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalysisTaskMockMvc.perform(post("/api/analysis-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisTask)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisTask in the database
        List<AnalysisTask> analysisTaskList = analysisTaskRepository.findAll();
        assertThat(analysisTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAnalysisTasks() throws Exception {
        // Initialize the database
        analysisTaskRepository.saveAndFlush(analysisTask);

        // Get all the analysisTaskList
        restAnalysisTaskMockMvc.perform(get("/api/analysis-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].analysisType").value(hasItem(DEFAULT_ANALYSIS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].analysisStatus").value(hasItem(DEFAULT_ANALYSIS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].videoType").value(hasItem(DEFAULT_VIDEO_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].executionAt").value(hasItem(sameInstant(DEFAULT_EXECUTION_AT))));
    }

    @Test
    @Transactional
    public void getAnalysisTask() throws Exception {
        // Initialize the database
        analysisTaskRepository.saveAndFlush(analysisTask);

        // Get the analysisTask
        restAnalysisTaskMockMvc.perform(get("/api/analysis-tasks/{id}", analysisTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analysisTask.getId().intValue()))
            .andExpect(jsonPath("$.analysisType").value(DEFAULT_ANALYSIS_TYPE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.analysisStatus").value(DEFAULT_ANALYSIS_STATUS.toString()))
            .andExpect(jsonPath("$.videoType").value(DEFAULT_VIDEO_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.executionAt").value(sameInstant(DEFAULT_EXECUTION_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingAnalysisTask() throws Exception {
        // Get the analysisTask
        restAnalysisTaskMockMvc.perform(get("/api/analysis-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalysisTask() throws Exception {
        // Initialize the database
        analysisTaskRepository.saveAndFlush(analysisTask);
        analysisTaskSearchRepository.save(analysisTask);
        int databaseSizeBeforeUpdate = analysisTaskRepository.findAll().size();

        // Update the analysisTask
        AnalysisTask updatedAnalysisTask = analysisTaskRepository.findOne(analysisTask.getId());
        // Disconnect from session so that the updates on updatedAnalysisTask are not directly saved in db
        em.detach(updatedAnalysisTask);
        updatedAnalysisTask
            .analysisType(UPDATED_ANALYSIS_TYPE)
            .priority(UPDATED_PRIORITY)
            .analysisStatus(UPDATED_ANALYSIS_STATUS)
            .videoType(UPDATED_VIDEO_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .executionAt(UPDATED_EXECUTION_AT);

        restAnalysisTaskMockMvc.perform(put("/api/analysis-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalysisTask)))
            .andExpect(status().isOk());

        // Validate the AnalysisTask in the database
        List<AnalysisTask> analysisTaskList = analysisTaskRepository.findAll();
        assertThat(analysisTaskList).hasSize(databaseSizeBeforeUpdate);
        AnalysisTask testAnalysisTask = analysisTaskList.get(analysisTaskList.size() - 1);
        assertThat(testAnalysisTask.getAnalysisType()).isEqualTo(UPDATED_ANALYSIS_TYPE);
        assertThat(testAnalysisTask.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testAnalysisTask.getAnalysisStatus()).isEqualTo(UPDATED_ANALYSIS_STATUS);
        assertThat(testAnalysisTask.getVideoType()).isEqualTo(UPDATED_VIDEO_TYPE);
        assertThat(testAnalysisTask.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAnalysisTask.getExecutionAt()).isEqualTo(UPDATED_EXECUTION_AT);

        // Validate the AnalysisTask in Elasticsearch
        AnalysisTask analysisTaskEs = analysisTaskSearchRepository.findOne(testAnalysisTask.getId());
        assertThat(testAnalysisTask.getCreatedAt()).isEqualTo(testAnalysisTask.getCreatedAt());
        assertThat(testAnalysisTask.getExecutionAt()).isEqualTo(testAnalysisTask.getExecutionAt());
        assertThat(analysisTaskEs).isEqualToIgnoringGivenFields(testAnalysisTask, "createdAt", "executionAt");
    }

    @Test
    @Transactional
    public void updateNonExistingAnalysisTask() throws Exception {
        int databaseSizeBeforeUpdate = analysisTaskRepository.findAll().size();

        // Create the AnalysisTask

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnalysisTaskMockMvc.perform(put("/api/analysis-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisTask)))
            .andExpect(status().isCreated());

        // Validate the AnalysisTask in the database
        List<AnalysisTask> analysisTaskList = analysisTaskRepository.findAll();
        assertThat(analysisTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnalysisTask() throws Exception {
        // Initialize the database
        analysisTaskRepository.saveAndFlush(analysisTask);
        analysisTaskSearchRepository.save(analysisTask);
        int databaseSizeBeforeDelete = analysisTaskRepository.findAll().size();

        // Get the analysisTask
        restAnalysisTaskMockMvc.perform(delete("/api/analysis-tasks/{id}", analysisTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean analysisTaskExistsInEs = analysisTaskSearchRepository.exists(analysisTask.getId());
        assertThat(analysisTaskExistsInEs).isFalse();

        // Validate the database is empty
        List<AnalysisTask> analysisTaskList = analysisTaskRepository.findAll();
        assertThat(analysisTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAnalysisTask() throws Exception {
        // Initialize the database
        analysisTaskRepository.saveAndFlush(analysisTask);
        analysisTaskSearchRepository.save(analysisTask);

        // Search the analysisTask
        restAnalysisTaskMockMvc.perform(get("/api/_search/analysis-tasks?query=id:" + analysisTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].analysisType").value(hasItem(DEFAULT_ANALYSIS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].analysisStatus").value(hasItem(DEFAULT_ANALYSIS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].videoType").value(hasItem(DEFAULT_VIDEO_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].executionAt").value(hasItem(sameInstant(DEFAULT_EXECUTION_AT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalysisTask.class);
        AnalysisTask analysisTask1 = new AnalysisTask();
        analysisTask1.setId(1L);
        AnalysisTask analysisTask2 = new AnalysisTask();
        analysisTask2.setId(analysisTask1.getId());
        assertThat(analysisTask1).isEqualTo(analysisTask2);
        analysisTask2.setId(2L);
        assertThat(analysisTask1).isNotEqualTo(analysisTask2);
        analysisTask1.setId(null);
        assertThat(analysisTask1).isNotEqualTo(analysisTask2);
    }
}
