package com.cetc.cctv.web.rest;

import com.cetc.cctv.CtecApp;

import com.cetc.cctv.domain.Camera;
import com.cetc.cctv.repository.CameraRepository;
import com.cetc.cctv.repository.search.CameraSearchRepository;
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

import com.cetc.cctv.domain.enumeration.DeviceStatus;
import com.cetc.cctv.domain.enumeration.FilterType;
/**
 * Test class for the CameraResource REST controller.
 *
 * @see CameraResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CtecApp.class)
public class CameraResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CAMERA_ID = "AAAAAAAAAA";
    private static final String UPDATED_CAMERA_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CAMERA_IP = "AAAAAAAAAA";
    private static final String UPDATED_CAMERA_IP = "BBBBBBBBBB";

    private static final String DEFAULT_BELONG_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_BELONG_SERVER = "BBBBBBBBBB";

    private static final String DEFAULT_BELONG_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_BELONG_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_COLLECT_STANDARDS = "AAAAAAAAAA";
    private static final String UPDATED_COLLECT_STANDARDS = "BBBBBBBBBB";

    private static final DeviceStatus DEFAULT_DEVICE_STATUS = DeviceStatus.RUNNING;
    private static final DeviceStatus UPDATED_DEVICE_STATUS = DeviceStatus.STOP;

    private static final Boolean DEFAULT_FAST_RUN_WARN = false;
    private static final Boolean UPDATED_FAST_RUN_WARN = true;

    private static final Boolean DEFAULT_PEOPLE_COUNT_LIMIT_WARN = false;
    private static final Boolean UPDATED_PEOPLE_COUNT_LIMIT_WARN = true;

    private static final Boolean DEFAULT_CROWDS_GATHER_WARN = false;
    private static final Boolean UPDATED_CROWDS_GATHER_WARN = true;

    private static final Boolean DEFAULT_VIGOROUSLY_WAVED_WARN = false;
    private static final Boolean UPDATED_VIGOROUSLY_WAVED_WARN = true;

    private static final Boolean DEFAULT_FIGHT_WARN = false;
    private static final Boolean UPDATED_FIGHT_WARN = true;

    private static final Boolean DEFAULT_ABNORMAL_ACTION_WARN = false;
    private static final Boolean UPDATED_ABNORMAL_ACTION_WARN = true;

    private static final Integer DEFAULT_FAST_RUN_WARN_LIMIT = 1;
    private static final Integer UPDATED_FAST_RUN_WARN_LIMIT = 2;

    private static final Integer DEFAULT_PEOPLE_COUNT_WARN_LIMIT = 1;
    private static final Integer UPDATED_PEOPLE_COUNT_WARN_LIMIT = 2;

    private static final Integer DEFAULT_CROWDS_GATHER_WARN_LIMIT = 1;
    private static final Integer UPDATED_CROWDS_GATHER_WARN_LIMIT = 2;

    private static final Integer DEFAULT_VIGOROUSLY_WAVED_WARN_LIMIT = 1;
    private static final Integer UPDATED_VIGOROUSLY_WAVED_WARN_LIMIT = 2;

    private static final Integer DEFAULT_FIGHT_WARN_LIMIT = 1;
    private static final Integer UPDATED_FIGHT_WARN_LIMIT = 2;

    private static final Integer DEFAULT_ABNORMAL_ACTION_WARN_LIMIT = 1;
    private static final Integer UPDATED_ABNORMAL_ACTION_WARN_LIMIT = 2;

    private static final FilterType DEFAULT_FILTER_TYPE = FilterType.HAIRSTYLE;
    private static final FilterType UPDATED_FILTER_TYPE = FilterType.SEX;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private CameraSearchRepository cameraSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCameraMockMvc;

    private Camera camera;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CameraResource cameraResource = new CameraResource(cameraRepository, cameraSearchRepository);
        this.restCameraMockMvc = MockMvcBuilders.standaloneSetup(cameraResource)
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
    public static Camera createEntity(EntityManager em) {
        Camera camera = new Camera()
            .name(DEFAULT_NAME)
            .cameraID(DEFAULT_CAMERA_ID)
            .cameraIP(DEFAULT_CAMERA_IP)
            .belongServer(DEFAULT_BELONG_SERVER)
            .belongChannel(DEFAULT_BELONG_CHANNEL)
            .collectStandards(DEFAULT_COLLECT_STANDARDS)
            .deviceStatus(DEFAULT_DEVICE_STATUS)
            .fastRunWarn(DEFAULT_FAST_RUN_WARN)
            .peopleCountLimitWarn(DEFAULT_PEOPLE_COUNT_LIMIT_WARN)
            .crowdsGatherWarn(DEFAULT_CROWDS_GATHER_WARN)
            .vigorouslyWavedWarn(DEFAULT_VIGOROUSLY_WAVED_WARN)
            .fightWarn(DEFAULT_FIGHT_WARN)
            .abnormalActionWarn(DEFAULT_ABNORMAL_ACTION_WARN)
            .fastRunWarnLimit(DEFAULT_FAST_RUN_WARN_LIMIT)
            .peopleCountWarnLimit(DEFAULT_PEOPLE_COUNT_WARN_LIMIT)
            .crowdsGatherWarnLimit(DEFAULT_CROWDS_GATHER_WARN_LIMIT)
            .vigorouslyWavedWarnLimit(DEFAULT_VIGOROUSLY_WAVED_WARN_LIMIT)
            .fightWarnLimit(DEFAULT_FIGHT_WARN_LIMIT)
            .abnormalActionWarnLimit(DEFAULT_ABNORMAL_ACTION_WARN_LIMIT)
            .filterType(DEFAULT_FILTER_TYPE);
        return camera;
    }

    @Before
    public void initTest() {
        cameraSearchRepository.deleteAll();
        camera = createEntity(em);
    }

    @Test
    @Transactional
    public void createCamera() throws Exception {
        int databaseSizeBeforeCreate = cameraRepository.findAll().size();

        // Create the Camera
        restCameraMockMvc.perform(post("/api/cameras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(camera)))
            .andExpect(status().isCreated());

        // Validate the Camera in the database
        List<Camera> cameraList = cameraRepository.findAll();
        assertThat(cameraList).hasSize(databaseSizeBeforeCreate + 1);
        Camera testCamera = cameraList.get(cameraList.size() - 1);
        assertThat(testCamera.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCamera.getCameraID()).isEqualTo(DEFAULT_CAMERA_ID);
        assertThat(testCamera.getCameraIP()).isEqualTo(DEFAULT_CAMERA_IP);
        assertThat(testCamera.getBelongServer()).isEqualTo(DEFAULT_BELONG_SERVER);
        assertThat(testCamera.getBelongChannel()).isEqualTo(DEFAULT_BELONG_CHANNEL);
        assertThat(testCamera.getCollectStandards()).isEqualTo(DEFAULT_COLLECT_STANDARDS);
        assertThat(testCamera.getDeviceStatus()).isEqualTo(DEFAULT_DEVICE_STATUS);
        assertThat(testCamera.isFastRunWarn()).isEqualTo(DEFAULT_FAST_RUN_WARN);
        assertThat(testCamera.isPeopleCountLimitWarn()).isEqualTo(DEFAULT_PEOPLE_COUNT_LIMIT_WARN);
        assertThat(testCamera.isCrowdsGatherWarn()).isEqualTo(DEFAULT_CROWDS_GATHER_WARN);
        assertThat(testCamera.isVigorouslyWavedWarn()).isEqualTo(DEFAULT_VIGOROUSLY_WAVED_WARN);
        assertThat(testCamera.isFightWarn()).isEqualTo(DEFAULT_FIGHT_WARN);
        assertThat(testCamera.isAbnormalActionWarn()).isEqualTo(DEFAULT_ABNORMAL_ACTION_WARN);
        assertThat(testCamera.getFastRunWarnLimit()).isEqualTo(DEFAULT_FAST_RUN_WARN_LIMIT);
        assertThat(testCamera.getPeopleCountWarnLimit()).isEqualTo(DEFAULT_PEOPLE_COUNT_WARN_LIMIT);
        assertThat(testCamera.getCrowdsGatherWarnLimit()).isEqualTo(DEFAULT_CROWDS_GATHER_WARN_LIMIT);
        assertThat(testCamera.getVigorouslyWavedWarnLimit()).isEqualTo(DEFAULT_VIGOROUSLY_WAVED_WARN_LIMIT);
        assertThat(testCamera.getFightWarnLimit()).isEqualTo(DEFAULT_FIGHT_WARN_LIMIT);
        assertThat(testCamera.getAbnormalActionWarnLimit()).isEqualTo(DEFAULT_ABNORMAL_ACTION_WARN_LIMIT);
        assertThat(testCamera.getFilterType()).isEqualTo(DEFAULT_FILTER_TYPE);

        // Validate the Camera in Elasticsearch
        Camera cameraEs = cameraSearchRepository.findOne(testCamera.getId());
        assertThat(cameraEs).isEqualToIgnoringGivenFields(testCamera);
    }

    @Test
    @Transactional
    public void createCameraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cameraRepository.findAll().size();

        // Create the Camera with an existing ID
        camera.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCameraMockMvc.perform(post("/api/cameras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(camera)))
            .andExpect(status().isBadRequest());

        // Validate the Camera in the database
        List<Camera> cameraList = cameraRepository.findAll();
        assertThat(cameraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCameras() throws Exception {
        // Initialize the database
        cameraRepository.saveAndFlush(camera);

        // Get all the cameraList
        restCameraMockMvc.perform(get("/api/cameras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(camera.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cameraID").value(hasItem(DEFAULT_CAMERA_ID.toString())))
            .andExpect(jsonPath("$.[*].cameraIP").value(hasItem(DEFAULT_CAMERA_IP.toString())))
            .andExpect(jsonPath("$.[*].belongServer").value(hasItem(DEFAULT_BELONG_SERVER.toString())))
            .andExpect(jsonPath("$.[*].belongChannel").value(hasItem(DEFAULT_BELONG_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].collectStandards").value(hasItem(DEFAULT_COLLECT_STANDARDS.toString())))
            .andExpect(jsonPath("$.[*].deviceStatus").value(hasItem(DEFAULT_DEVICE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fastRunWarn").value(hasItem(DEFAULT_FAST_RUN_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].peopleCountLimitWarn").value(hasItem(DEFAULT_PEOPLE_COUNT_LIMIT_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].crowdsGatherWarn").value(hasItem(DEFAULT_CROWDS_GATHER_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].vigorouslyWavedWarn").value(hasItem(DEFAULT_VIGOROUSLY_WAVED_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].fightWarn").value(hasItem(DEFAULT_FIGHT_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].abnormalActionWarn").value(hasItem(DEFAULT_ABNORMAL_ACTION_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].fastRunWarnLimit").value(hasItem(DEFAULT_FAST_RUN_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].peopleCountWarnLimit").value(hasItem(DEFAULT_PEOPLE_COUNT_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].crowdsGatherWarnLimit").value(hasItem(DEFAULT_CROWDS_GATHER_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].vigorouslyWavedWarnLimit").value(hasItem(DEFAULT_VIGOROUSLY_WAVED_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].fightWarnLimit").value(hasItem(DEFAULT_FIGHT_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].abnormalActionWarnLimit").value(hasItem(DEFAULT_ABNORMAL_ACTION_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].filterType").value(hasItem(DEFAULT_FILTER_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCamera() throws Exception {
        // Initialize the database
        cameraRepository.saveAndFlush(camera);

        // Get the camera
        restCameraMockMvc.perform(get("/api/cameras/{id}", camera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(camera.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cameraID").value(DEFAULT_CAMERA_ID.toString()))
            .andExpect(jsonPath("$.cameraIP").value(DEFAULT_CAMERA_IP.toString()))
            .andExpect(jsonPath("$.belongServer").value(DEFAULT_BELONG_SERVER.toString()))
            .andExpect(jsonPath("$.belongChannel").value(DEFAULT_BELONG_CHANNEL.toString()))
            .andExpect(jsonPath("$.collectStandards").value(DEFAULT_COLLECT_STANDARDS.toString()))
            .andExpect(jsonPath("$.deviceStatus").value(DEFAULT_DEVICE_STATUS.toString()))
            .andExpect(jsonPath("$.fastRunWarn").value(DEFAULT_FAST_RUN_WARN.booleanValue()))
            .andExpect(jsonPath("$.peopleCountLimitWarn").value(DEFAULT_PEOPLE_COUNT_LIMIT_WARN.booleanValue()))
            .andExpect(jsonPath("$.crowdsGatherWarn").value(DEFAULT_CROWDS_GATHER_WARN.booleanValue()))
            .andExpect(jsonPath("$.vigorouslyWavedWarn").value(DEFAULT_VIGOROUSLY_WAVED_WARN.booleanValue()))
            .andExpect(jsonPath("$.fightWarn").value(DEFAULT_FIGHT_WARN.booleanValue()))
            .andExpect(jsonPath("$.abnormalActionWarn").value(DEFAULT_ABNORMAL_ACTION_WARN.booleanValue()))
            .andExpect(jsonPath("$.fastRunWarnLimit").value(DEFAULT_FAST_RUN_WARN_LIMIT))
            .andExpect(jsonPath("$.peopleCountWarnLimit").value(DEFAULT_PEOPLE_COUNT_WARN_LIMIT))
            .andExpect(jsonPath("$.crowdsGatherWarnLimit").value(DEFAULT_CROWDS_GATHER_WARN_LIMIT))
            .andExpect(jsonPath("$.vigorouslyWavedWarnLimit").value(DEFAULT_VIGOROUSLY_WAVED_WARN_LIMIT))
            .andExpect(jsonPath("$.fightWarnLimit").value(DEFAULT_FIGHT_WARN_LIMIT))
            .andExpect(jsonPath("$.abnormalActionWarnLimit").value(DEFAULT_ABNORMAL_ACTION_WARN_LIMIT))
            .andExpect(jsonPath("$.filterType").value(DEFAULT_FILTER_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCamera() throws Exception {
        // Get the camera
        restCameraMockMvc.perform(get("/api/cameras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCamera() throws Exception {
        // Initialize the database
        cameraRepository.saveAndFlush(camera);
        cameraSearchRepository.save(camera);
        int databaseSizeBeforeUpdate = cameraRepository.findAll().size();

        // Update the camera
        Camera updatedCamera = cameraRepository.findOne(camera.getId());
        // Disconnect from session so that the updates on updatedCamera are not directly saved in db
        em.detach(updatedCamera);
        updatedCamera
            .name(UPDATED_NAME)
            .cameraID(UPDATED_CAMERA_ID)
            .cameraIP(UPDATED_CAMERA_IP)
            .belongServer(UPDATED_BELONG_SERVER)
            .belongChannel(UPDATED_BELONG_CHANNEL)
            .collectStandards(UPDATED_COLLECT_STANDARDS)
            .deviceStatus(UPDATED_DEVICE_STATUS)
            .fastRunWarn(UPDATED_FAST_RUN_WARN)
            .peopleCountLimitWarn(UPDATED_PEOPLE_COUNT_LIMIT_WARN)
            .crowdsGatherWarn(UPDATED_CROWDS_GATHER_WARN)
            .vigorouslyWavedWarn(UPDATED_VIGOROUSLY_WAVED_WARN)
            .fightWarn(UPDATED_FIGHT_WARN)
            .abnormalActionWarn(UPDATED_ABNORMAL_ACTION_WARN)
            .fastRunWarnLimit(UPDATED_FAST_RUN_WARN_LIMIT)
            .peopleCountWarnLimit(UPDATED_PEOPLE_COUNT_WARN_LIMIT)
            .crowdsGatherWarnLimit(UPDATED_CROWDS_GATHER_WARN_LIMIT)
            .vigorouslyWavedWarnLimit(UPDATED_VIGOROUSLY_WAVED_WARN_LIMIT)
            .fightWarnLimit(UPDATED_FIGHT_WARN_LIMIT)
            .abnormalActionWarnLimit(UPDATED_ABNORMAL_ACTION_WARN_LIMIT)
            .filterType(UPDATED_FILTER_TYPE);

        restCameraMockMvc.perform(put("/api/cameras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCamera)))
            .andExpect(status().isOk());

        // Validate the Camera in the database
        List<Camera> cameraList = cameraRepository.findAll();
        assertThat(cameraList).hasSize(databaseSizeBeforeUpdate);
        Camera testCamera = cameraList.get(cameraList.size() - 1);
        assertThat(testCamera.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCamera.getCameraID()).isEqualTo(UPDATED_CAMERA_ID);
        assertThat(testCamera.getCameraIP()).isEqualTo(UPDATED_CAMERA_IP);
        assertThat(testCamera.getBelongServer()).isEqualTo(UPDATED_BELONG_SERVER);
        assertThat(testCamera.getBelongChannel()).isEqualTo(UPDATED_BELONG_CHANNEL);
        assertThat(testCamera.getCollectStandards()).isEqualTo(UPDATED_COLLECT_STANDARDS);
        assertThat(testCamera.getDeviceStatus()).isEqualTo(UPDATED_DEVICE_STATUS);
        assertThat(testCamera.isFastRunWarn()).isEqualTo(UPDATED_FAST_RUN_WARN);
        assertThat(testCamera.isPeopleCountLimitWarn()).isEqualTo(UPDATED_PEOPLE_COUNT_LIMIT_WARN);
        assertThat(testCamera.isCrowdsGatherWarn()).isEqualTo(UPDATED_CROWDS_GATHER_WARN);
        assertThat(testCamera.isVigorouslyWavedWarn()).isEqualTo(UPDATED_VIGOROUSLY_WAVED_WARN);
        assertThat(testCamera.isFightWarn()).isEqualTo(UPDATED_FIGHT_WARN);
        assertThat(testCamera.isAbnormalActionWarn()).isEqualTo(UPDATED_ABNORMAL_ACTION_WARN);
        assertThat(testCamera.getFastRunWarnLimit()).isEqualTo(UPDATED_FAST_RUN_WARN_LIMIT);
        assertThat(testCamera.getPeopleCountWarnLimit()).isEqualTo(UPDATED_PEOPLE_COUNT_WARN_LIMIT);
        assertThat(testCamera.getCrowdsGatherWarnLimit()).isEqualTo(UPDATED_CROWDS_GATHER_WARN_LIMIT);
        assertThat(testCamera.getVigorouslyWavedWarnLimit()).isEqualTo(UPDATED_VIGOROUSLY_WAVED_WARN_LIMIT);
        assertThat(testCamera.getFightWarnLimit()).isEqualTo(UPDATED_FIGHT_WARN_LIMIT);
        assertThat(testCamera.getAbnormalActionWarnLimit()).isEqualTo(UPDATED_ABNORMAL_ACTION_WARN_LIMIT);
        assertThat(testCamera.getFilterType()).isEqualTo(UPDATED_FILTER_TYPE);

        // Validate the Camera in Elasticsearch
        Camera cameraEs = cameraSearchRepository.findOne(testCamera.getId());
        assertThat(cameraEs).isEqualToIgnoringGivenFields(testCamera);
    }

    @Test
    @Transactional
    public void updateNonExistingCamera() throws Exception {
        int databaseSizeBeforeUpdate = cameraRepository.findAll().size();

        // Create the Camera

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCameraMockMvc.perform(put("/api/cameras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(camera)))
            .andExpect(status().isCreated());

        // Validate the Camera in the database
        List<Camera> cameraList = cameraRepository.findAll();
        assertThat(cameraList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCamera() throws Exception {
        // Initialize the database
        cameraRepository.saveAndFlush(camera);
        cameraSearchRepository.save(camera);
        int databaseSizeBeforeDelete = cameraRepository.findAll().size();

        // Get the camera
        restCameraMockMvc.perform(delete("/api/cameras/{id}", camera.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cameraExistsInEs = cameraSearchRepository.exists(camera.getId());
        assertThat(cameraExistsInEs).isFalse();

        // Validate the database is empty
        List<Camera> cameraList = cameraRepository.findAll();
        assertThat(cameraList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCamera() throws Exception {
        // Initialize the database
        cameraRepository.saveAndFlush(camera);
        cameraSearchRepository.save(camera);

        // Search the camera
        restCameraMockMvc.perform(get("/api/_search/cameras?query=id:" + camera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(camera.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cameraID").value(hasItem(DEFAULT_CAMERA_ID.toString())))
            .andExpect(jsonPath("$.[*].cameraIP").value(hasItem(DEFAULT_CAMERA_IP.toString())))
            .andExpect(jsonPath("$.[*].belongServer").value(hasItem(DEFAULT_BELONG_SERVER.toString())))
            .andExpect(jsonPath("$.[*].belongChannel").value(hasItem(DEFAULT_BELONG_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].collectStandards").value(hasItem(DEFAULT_COLLECT_STANDARDS.toString())))
            .andExpect(jsonPath("$.[*].deviceStatus").value(hasItem(DEFAULT_DEVICE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fastRunWarn").value(hasItem(DEFAULT_FAST_RUN_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].peopleCountLimitWarn").value(hasItem(DEFAULT_PEOPLE_COUNT_LIMIT_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].crowdsGatherWarn").value(hasItem(DEFAULT_CROWDS_GATHER_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].vigorouslyWavedWarn").value(hasItem(DEFAULT_VIGOROUSLY_WAVED_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].fightWarn").value(hasItem(DEFAULT_FIGHT_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].abnormalActionWarn").value(hasItem(DEFAULT_ABNORMAL_ACTION_WARN.booleanValue())))
            .andExpect(jsonPath("$.[*].fastRunWarnLimit").value(hasItem(DEFAULT_FAST_RUN_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].peopleCountWarnLimit").value(hasItem(DEFAULT_PEOPLE_COUNT_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].crowdsGatherWarnLimit").value(hasItem(DEFAULT_CROWDS_GATHER_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].vigorouslyWavedWarnLimit").value(hasItem(DEFAULT_VIGOROUSLY_WAVED_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].fightWarnLimit").value(hasItem(DEFAULT_FIGHT_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].abnormalActionWarnLimit").value(hasItem(DEFAULT_ABNORMAL_ACTION_WARN_LIMIT)))
            .andExpect(jsonPath("$.[*].filterType").value(hasItem(DEFAULT_FILTER_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Camera.class);
        Camera camera1 = new Camera();
        camera1.setId(1L);
        Camera camera2 = new Camera();
        camera2.setId(camera1.getId());
        assertThat(camera1).isEqualTo(camera2);
        camera2.setId(2L);
        assertThat(camera1).isNotEqualTo(camera2);
        camera1.setId(null);
        assertThat(camera1).isNotEqualTo(camera2);
    }
}
