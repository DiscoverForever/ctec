package com.cetc.cctv.web.rest;

import com.cetc.cctv.CtecApp;

import com.cetc.cctv.domain.PerimeterProtectRegion;
import com.cetc.cctv.repository.PerimeterProtectRegionRepository;
import com.cetc.cctv.repository.search.PerimeterProtectRegionSearchRepository;
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
 * Test class for the PerimeterProtectRegionResource REST controller.
 *
 * @see PerimeterProtectRegionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CtecApp.class)
public class PerimeterProtectRegionResourceIntTest {

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
    private PerimeterProtectRegionRepository perimeterProtectRegionRepository;

    @Autowired
    private PerimeterProtectRegionSearchRepository perimeterProtectRegionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPerimeterProtectRegionMockMvc;

    private PerimeterProtectRegion perimeterProtectRegion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PerimeterProtectRegionResource perimeterProtectRegionResource = new PerimeterProtectRegionResource(perimeterProtectRegionRepository, perimeterProtectRegionSearchRepository);
        this.restPerimeterProtectRegionMockMvc = MockMvcBuilders.standaloneSetup(perimeterProtectRegionResource)
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
    public static PerimeterProtectRegion createEntity(EntityManager em) {
        PerimeterProtectRegion perimeterProtectRegion = new PerimeterProtectRegion()
            .leftUpX(DEFAULT_LEFT_UP_X)
            .leftUpY(DEFAULT_LEFT_UP_Y)
            .rightUpX(DEFAULT_RIGHT_UP_X)
            .rightUpY(DEFAULT_RIGHT_UP_Y)
            .leftDownX(DEFAULT_LEFT_DOWN_X)
            .leftDownY(DEFAULT_LEFT_DOWN_Y)
            .rightDownX(DEFAULT_RIGHT_DOWN_X)
            .rightDownY(DEFAULT_RIGHT_DOWN_Y);
        return perimeterProtectRegion;
    }

    @Before
    public void initTest() {
        perimeterProtectRegionSearchRepository.deleteAll();
        perimeterProtectRegion = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerimeterProtectRegion() throws Exception {
        int databaseSizeBeforeCreate = perimeterProtectRegionRepository.findAll().size();

        // Create the PerimeterProtectRegion
        restPerimeterProtectRegionMockMvc.perform(post("/api/perimeter-protect-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perimeterProtectRegion)))
            .andExpect(status().isCreated());

        // Validate the PerimeterProtectRegion in the database
        List<PerimeterProtectRegion> perimeterProtectRegionList = perimeterProtectRegionRepository.findAll();
        assertThat(perimeterProtectRegionList).hasSize(databaseSizeBeforeCreate + 1);
        PerimeterProtectRegion testPerimeterProtectRegion = perimeterProtectRegionList.get(perimeterProtectRegionList.size() - 1);
        assertThat(testPerimeterProtectRegion.getLeftUpX()).isEqualTo(DEFAULT_LEFT_UP_X);
        assertThat(testPerimeterProtectRegion.getLeftUpY()).isEqualTo(DEFAULT_LEFT_UP_Y);
        assertThat(testPerimeterProtectRegion.getRightUpX()).isEqualTo(DEFAULT_RIGHT_UP_X);
        assertThat(testPerimeterProtectRegion.getRightUpY()).isEqualTo(DEFAULT_RIGHT_UP_Y);
        assertThat(testPerimeterProtectRegion.getLeftDownX()).isEqualTo(DEFAULT_LEFT_DOWN_X);
        assertThat(testPerimeterProtectRegion.getLeftDownY()).isEqualTo(DEFAULT_LEFT_DOWN_Y);
        assertThat(testPerimeterProtectRegion.getRightDownX()).isEqualTo(DEFAULT_RIGHT_DOWN_X);
        assertThat(testPerimeterProtectRegion.getRightDownY()).isEqualTo(DEFAULT_RIGHT_DOWN_Y);

        // Validate the PerimeterProtectRegion in Elasticsearch
        PerimeterProtectRegion perimeterProtectRegionEs = perimeterProtectRegionSearchRepository.findOne(testPerimeterProtectRegion.getId());
        assertThat(perimeterProtectRegionEs).isEqualToIgnoringGivenFields(testPerimeterProtectRegion);
    }

    @Test
    @Transactional
    public void createPerimeterProtectRegionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = perimeterProtectRegionRepository.findAll().size();

        // Create the PerimeterProtectRegion with an existing ID
        perimeterProtectRegion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerimeterProtectRegionMockMvc.perform(post("/api/perimeter-protect-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perimeterProtectRegion)))
            .andExpect(status().isBadRequest());

        // Validate the PerimeterProtectRegion in the database
        List<PerimeterProtectRegion> perimeterProtectRegionList = perimeterProtectRegionRepository.findAll();
        assertThat(perimeterProtectRegionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPerimeterProtectRegions() throws Exception {
        // Initialize the database
        perimeterProtectRegionRepository.saveAndFlush(perimeterProtectRegion);

        // Get all the perimeterProtectRegionList
        restPerimeterProtectRegionMockMvc.perform(get("/api/perimeter-protect-regions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perimeterProtectRegion.getId().intValue())))
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
    public void getPerimeterProtectRegion() throws Exception {
        // Initialize the database
        perimeterProtectRegionRepository.saveAndFlush(perimeterProtectRegion);

        // Get the perimeterProtectRegion
        restPerimeterProtectRegionMockMvc.perform(get("/api/perimeter-protect-regions/{id}", perimeterProtectRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(perimeterProtectRegion.getId().intValue()))
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
    public void getNonExistingPerimeterProtectRegion() throws Exception {
        // Get the perimeterProtectRegion
        restPerimeterProtectRegionMockMvc.perform(get("/api/perimeter-protect-regions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerimeterProtectRegion() throws Exception {
        // Initialize the database
        perimeterProtectRegionRepository.saveAndFlush(perimeterProtectRegion);
        perimeterProtectRegionSearchRepository.save(perimeterProtectRegion);
        int databaseSizeBeforeUpdate = perimeterProtectRegionRepository.findAll().size();

        // Update the perimeterProtectRegion
        PerimeterProtectRegion updatedPerimeterProtectRegion = perimeterProtectRegionRepository.findOne(perimeterProtectRegion.getId());
        // Disconnect from session so that the updates on updatedPerimeterProtectRegion are not directly saved in db
        em.detach(updatedPerimeterProtectRegion);
        updatedPerimeterProtectRegion
            .leftUpX(UPDATED_LEFT_UP_X)
            .leftUpY(UPDATED_LEFT_UP_Y)
            .rightUpX(UPDATED_RIGHT_UP_X)
            .rightUpY(UPDATED_RIGHT_UP_Y)
            .leftDownX(UPDATED_LEFT_DOWN_X)
            .leftDownY(UPDATED_LEFT_DOWN_Y)
            .rightDownX(UPDATED_RIGHT_DOWN_X)
            .rightDownY(UPDATED_RIGHT_DOWN_Y);

        restPerimeterProtectRegionMockMvc.perform(put("/api/perimeter-protect-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPerimeterProtectRegion)))
            .andExpect(status().isOk());

        // Validate the PerimeterProtectRegion in the database
        List<PerimeterProtectRegion> perimeterProtectRegionList = perimeterProtectRegionRepository.findAll();
        assertThat(perimeterProtectRegionList).hasSize(databaseSizeBeforeUpdate);
        PerimeterProtectRegion testPerimeterProtectRegion = perimeterProtectRegionList.get(perimeterProtectRegionList.size() - 1);
        assertThat(testPerimeterProtectRegion.getLeftUpX()).isEqualTo(UPDATED_LEFT_UP_X);
        assertThat(testPerimeterProtectRegion.getLeftUpY()).isEqualTo(UPDATED_LEFT_UP_Y);
        assertThat(testPerimeterProtectRegion.getRightUpX()).isEqualTo(UPDATED_RIGHT_UP_X);
        assertThat(testPerimeterProtectRegion.getRightUpY()).isEqualTo(UPDATED_RIGHT_UP_Y);
        assertThat(testPerimeterProtectRegion.getLeftDownX()).isEqualTo(UPDATED_LEFT_DOWN_X);
        assertThat(testPerimeterProtectRegion.getLeftDownY()).isEqualTo(UPDATED_LEFT_DOWN_Y);
        assertThat(testPerimeterProtectRegion.getRightDownX()).isEqualTo(UPDATED_RIGHT_DOWN_X);
        assertThat(testPerimeterProtectRegion.getRightDownY()).isEqualTo(UPDATED_RIGHT_DOWN_Y);

        // Validate the PerimeterProtectRegion in Elasticsearch
        PerimeterProtectRegion perimeterProtectRegionEs = perimeterProtectRegionSearchRepository.findOne(testPerimeterProtectRegion.getId());
        assertThat(perimeterProtectRegionEs).isEqualToIgnoringGivenFields(testPerimeterProtectRegion);
    }

    @Test
    @Transactional
    public void updateNonExistingPerimeterProtectRegion() throws Exception {
        int databaseSizeBeforeUpdate = perimeterProtectRegionRepository.findAll().size();

        // Create the PerimeterProtectRegion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPerimeterProtectRegionMockMvc.perform(put("/api/perimeter-protect-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perimeterProtectRegion)))
            .andExpect(status().isCreated());

        // Validate the PerimeterProtectRegion in the database
        List<PerimeterProtectRegion> perimeterProtectRegionList = perimeterProtectRegionRepository.findAll();
        assertThat(perimeterProtectRegionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePerimeterProtectRegion() throws Exception {
        // Initialize the database
        perimeterProtectRegionRepository.saveAndFlush(perimeterProtectRegion);
        perimeterProtectRegionSearchRepository.save(perimeterProtectRegion);
        int databaseSizeBeforeDelete = perimeterProtectRegionRepository.findAll().size();

        // Get the perimeterProtectRegion
        restPerimeterProtectRegionMockMvc.perform(delete("/api/perimeter-protect-regions/{id}", perimeterProtectRegion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean perimeterProtectRegionExistsInEs = perimeterProtectRegionSearchRepository.exists(perimeterProtectRegion.getId());
        assertThat(perimeterProtectRegionExistsInEs).isFalse();

        // Validate the database is empty
        List<PerimeterProtectRegion> perimeterProtectRegionList = perimeterProtectRegionRepository.findAll();
        assertThat(perimeterProtectRegionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPerimeterProtectRegion() throws Exception {
        // Initialize the database
        perimeterProtectRegionRepository.saveAndFlush(perimeterProtectRegion);
        perimeterProtectRegionSearchRepository.save(perimeterProtectRegion);

        // Search the perimeterProtectRegion
        restPerimeterProtectRegionMockMvc.perform(get("/api/_search/perimeter-protect-regions?query=id:" + perimeterProtectRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perimeterProtectRegion.getId().intValue())))
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
        TestUtil.equalsVerifier(PerimeterProtectRegion.class);
        PerimeterProtectRegion perimeterProtectRegion1 = new PerimeterProtectRegion();
        perimeterProtectRegion1.setId(1L);
        PerimeterProtectRegion perimeterProtectRegion2 = new PerimeterProtectRegion();
        perimeterProtectRegion2.setId(perimeterProtectRegion1.getId());
        assertThat(perimeterProtectRegion1).isEqualTo(perimeterProtectRegion2);
        perimeterProtectRegion2.setId(2L);
        assertThat(perimeterProtectRegion1).isNotEqualTo(perimeterProtectRegion2);
        perimeterProtectRegion1.setId(null);
        assertThat(perimeterProtectRegion1).isNotEqualTo(perimeterProtectRegion2);
    }
}
