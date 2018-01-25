package com.cetc.cctv.web.rest;

import com.cetc.cctv.CtecApp;

import com.cetc.cctv.domain.Cluster;
import com.cetc.cctv.repository.ClusterRepository;
import com.cetc.cctv.repository.search.ClusterSearchRepository;
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
 * Test class for the ClusterResource REST controller.
 *
 * @see ClusterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CtecApp.class)
public class ClusterResourceIntTest {

    private static final String DEFAULT_VIDEO_SERVER_IP = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_SERVER_IP = "BBBBBBBBBB";

    private static final Integer DEFAULT_VIDEO_CHANNEL_NUMBER = 1;
    private static final Integer UPDATED_VIDEO_CHANNEL_NUMBER = 2;

    private static final Integer DEFAULT_VIDEO_SERVER_PORT = 1;
    private static final Integer UPDATED_VIDEO_SERVER_PORT = 2;

    private static final String DEFAULT_VIDEO_SERVER_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_SERVER_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_SERVER_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_SERVER_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_DB_IP = "AAAAAAAAAA";
    private static final String UPDATED_DB_IP = "BBBBBBBBBB";

    private static final String DEFAULT_DB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLUSTER_NODE_IP = "AAAAAAAAAA";
    private static final String UPDATED_CLUSTER_NODE_IP = "BBBBBBBBBB";

    private static final String DEFAULT_CLUSTER_NODE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLUSTER_NODE_NAME = "BBBBBBBBBB";

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private ClusterSearchRepository clusterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClusterMockMvc;

    private Cluster cluster;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClusterResource clusterResource = new ClusterResource(clusterRepository, clusterSearchRepository);
        this.restClusterMockMvc = MockMvcBuilders.standaloneSetup(clusterResource)
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
    public static Cluster createEntity(EntityManager em) {
        Cluster cluster = new Cluster()
            .videoServerIp(DEFAULT_VIDEO_SERVER_IP)
            .videoChannelNumber(DEFAULT_VIDEO_CHANNEL_NUMBER)
            .videoServerPort(DEFAULT_VIDEO_SERVER_PORT)
            .videoServerUsername(DEFAULT_VIDEO_SERVER_USERNAME)
            .videoServerPassword(DEFAULT_VIDEO_SERVER_PASSWORD)
            .dbIp(DEFAULT_DB_IP)
            .dbName(DEFAULT_DB_NAME)
            .clusterNodeIp(DEFAULT_CLUSTER_NODE_IP)
            .clusterNodeName(DEFAULT_CLUSTER_NODE_NAME);
        return cluster;
    }

    @Before
    public void initTest() {
        clusterSearchRepository.deleteAll();
        cluster = createEntity(em);
    }

    @Test
    @Transactional
    public void createCluster() throws Exception {
        int databaseSizeBeforeCreate = clusterRepository.findAll().size();

        // Create the Cluster
        restClusterMockMvc.perform(post("/api/clusters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isCreated());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate + 1);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getVideoServerIp()).isEqualTo(DEFAULT_VIDEO_SERVER_IP);
        assertThat(testCluster.getVideoChannelNumber()).isEqualTo(DEFAULT_VIDEO_CHANNEL_NUMBER);
        assertThat(testCluster.getVideoServerPort()).isEqualTo(DEFAULT_VIDEO_SERVER_PORT);
        assertThat(testCluster.getVideoServerUsername()).isEqualTo(DEFAULT_VIDEO_SERVER_USERNAME);
        assertThat(testCluster.getVideoServerPassword()).isEqualTo(DEFAULT_VIDEO_SERVER_PASSWORD);
        assertThat(testCluster.getDbIp()).isEqualTo(DEFAULT_DB_IP);
        assertThat(testCluster.getDbName()).isEqualTo(DEFAULT_DB_NAME);
        assertThat(testCluster.getClusterNodeIp()).isEqualTo(DEFAULT_CLUSTER_NODE_IP);
        assertThat(testCluster.getClusterNodeName()).isEqualTo(DEFAULT_CLUSTER_NODE_NAME);

        // Validate the Cluster in Elasticsearch
        Cluster clusterEs = clusterSearchRepository.findOne(testCluster.getId());
        assertThat(clusterEs).isEqualToIgnoringGivenFields(testCluster);
    }

    @Test
    @Transactional
    public void createClusterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clusterRepository.findAll().size();

        // Create the Cluster with an existing ID
        cluster.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClusterMockMvc.perform(post("/api/clusters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClusters() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList
        restClusterMockMvc.perform(get("/api/clusters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].videoServerIp").value(hasItem(DEFAULT_VIDEO_SERVER_IP.toString())))
            .andExpect(jsonPath("$.[*].videoChannelNumber").value(hasItem(DEFAULT_VIDEO_CHANNEL_NUMBER)))
            .andExpect(jsonPath("$.[*].videoServerPort").value(hasItem(DEFAULT_VIDEO_SERVER_PORT)))
            .andExpect(jsonPath("$.[*].videoServerUsername").value(hasItem(DEFAULT_VIDEO_SERVER_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].videoServerPassword").value(hasItem(DEFAULT_VIDEO_SERVER_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].dbIp").value(hasItem(DEFAULT_DB_IP.toString())))
            .andExpect(jsonPath("$.[*].dbName").value(hasItem(DEFAULT_DB_NAME.toString())))
            .andExpect(jsonPath("$.[*].clusterNodeIp").value(hasItem(DEFAULT_CLUSTER_NODE_IP.toString())))
            .andExpect(jsonPath("$.[*].clusterNodeName").value(hasItem(DEFAULT_CLUSTER_NODE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get the cluster
        restClusterMockMvc.perform(get("/api/clusters/{id}", cluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cluster.getId().intValue()))
            .andExpect(jsonPath("$.videoServerIp").value(DEFAULT_VIDEO_SERVER_IP.toString()))
            .andExpect(jsonPath("$.videoChannelNumber").value(DEFAULT_VIDEO_CHANNEL_NUMBER))
            .andExpect(jsonPath("$.videoServerPort").value(DEFAULT_VIDEO_SERVER_PORT))
            .andExpect(jsonPath("$.videoServerUsername").value(DEFAULT_VIDEO_SERVER_USERNAME.toString()))
            .andExpect(jsonPath("$.videoServerPassword").value(DEFAULT_VIDEO_SERVER_PASSWORD.toString()))
            .andExpect(jsonPath("$.dbIp").value(DEFAULT_DB_IP.toString()))
            .andExpect(jsonPath("$.dbName").value(DEFAULT_DB_NAME.toString()))
            .andExpect(jsonPath("$.clusterNodeIp").value(DEFAULT_CLUSTER_NODE_IP.toString()))
            .andExpect(jsonPath("$.clusterNodeName").value(DEFAULT_CLUSTER_NODE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCluster() throws Exception {
        // Get the cluster
        restClusterMockMvc.perform(get("/api/clusters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);
        clusterSearchRepository.save(cluster);
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster
        Cluster updatedCluster = clusterRepository.findOne(cluster.getId());
        // Disconnect from session so that the updates on updatedCluster are not directly saved in db
        em.detach(updatedCluster);
        updatedCluster
            .videoServerIp(UPDATED_VIDEO_SERVER_IP)
            .videoChannelNumber(UPDATED_VIDEO_CHANNEL_NUMBER)
            .videoServerPort(UPDATED_VIDEO_SERVER_PORT)
            .videoServerUsername(UPDATED_VIDEO_SERVER_USERNAME)
            .videoServerPassword(UPDATED_VIDEO_SERVER_PASSWORD)
            .dbIp(UPDATED_DB_IP)
            .dbName(UPDATED_DB_NAME)
            .clusterNodeIp(UPDATED_CLUSTER_NODE_IP)
            .clusterNodeName(UPDATED_CLUSTER_NODE_NAME);

        restClusterMockMvc.perform(put("/api/clusters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCluster)))
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getVideoServerIp()).isEqualTo(UPDATED_VIDEO_SERVER_IP);
        assertThat(testCluster.getVideoChannelNumber()).isEqualTo(UPDATED_VIDEO_CHANNEL_NUMBER);
        assertThat(testCluster.getVideoServerPort()).isEqualTo(UPDATED_VIDEO_SERVER_PORT);
        assertThat(testCluster.getVideoServerUsername()).isEqualTo(UPDATED_VIDEO_SERVER_USERNAME);
        assertThat(testCluster.getVideoServerPassword()).isEqualTo(UPDATED_VIDEO_SERVER_PASSWORD);
        assertThat(testCluster.getDbIp()).isEqualTo(UPDATED_DB_IP);
        assertThat(testCluster.getDbName()).isEqualTo(UPDATED_DB_NAME);
        assertThat(testCluster.getClusterNodeIp()).isEqualTo(UPDATED_CLUSTER_NODE_IP);
        assertThat(testCluster.getClusterNodeName()).isEqualTo(UPDATED_CLUSTER_NODE_NAME);

        // Validate the Cluster in Elasticsearch
        Cluster clusterEs = clusterSearchRepository.findOne(testCluster.getId());
        assertThat(clusterEs).isEqualToIgnoringGivenFields(testCluster);
    }

    @Test
    @Transactional
    public void updateNonExistingCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Create the Cluster

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClusterMockMvc.perform(put("/api/clusters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isCreated());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);
        clusterSearchRepository.save(cluster);
        int databaseSizeBeforeDelete = clusterRepository.findAll().size();

        // Get the cluster
        restClusterMockMvc.perform(delete("/api/clusters/{id}", cluster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean clusterExistsInEs = clusterSearchRepository.exists(cluster.getId());
        assertThat(clusterExistsInEs).isFalse();

        // Validate the database is empty
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);
        clusterSearchRepository.save(cluster);

        // Search the cluster
        restClusterMockMvc.perform(get("/api/_search/clusters?query=id:" + cluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].videoServerIp").value(hasItem(DEFAULT_VIDEO_SERVER_IP.toString())))
            .andExpect(jsonPath("$.[*].videoChannelNumber").value(hasItem(DEFAULT_VIDEO_CHANNEL_NUMBER)))
            .andExpect(jsonPath("$.[*].videoServerPort").value(hasItem(DEFAULT_VIDEO_SERVER_PORT)))
            .andExpect(jsonPath("$.[*].videoServerUsername").value(hasItem(DEFAULT_VIDEO_SERVER_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].videoServerPassword").value(hasItem(DEFAULT_VIDEO_SERVER_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].dbIp").value(hasItem(DEFAULT_DB_IP.toString())))
            .andExpect(jsonPath("$.[*].dbName").value(hasItem(DEFAULT_DB_NAME.toString())))
            .andExpect(jsonPath("$.[*].clusterNodeIp").value(hasItem(DEFAULT_CLUSTER_NODE_IP.toString())))
            .andExpect(jsonPath("$.[*].clusterNodeName").value(hasItem(DEFAULT_CLUSTER_NODE_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cluster.class);
        Cluster cluster1 = new Cluster();
        cluster1.setId(1L);
        Cluster cluster2 = new Cluster();
        cluster2.setId(cluster1.getId());
        assertThat(cluster1).isEqualTo(cluster2);
        cluster2.setId(2L);
        assertThat(cluster1).isNotEqualTo(cluster2);
        cluster1.setId(null);
        assertThat(cluster1).isNotEqualTo(cluster2);
    }
}
