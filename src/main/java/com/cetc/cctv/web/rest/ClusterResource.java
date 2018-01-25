package com.cetc.cctv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cetc.cctv.domain.Cluster;

import com.cetc.cctv.repository.ClusterRepository;
import com.cetc.cctv.repository.search.ClusterSearchRepository;
import com.cetc.cctv.web.rest.errors.BadRequestAlertException;
import com.cetc.cctv.web.rest.util.HeaderUtil;
import com.cetc.cctv.web.rest.util.PaginationUtil;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Cluster.
 */
@RestController
@RequestMapping("/api")
public class ClusterResource {

    private final Logger log = LoggerFactory.getLogger(ClusterResource.class);

    private static final String ENTITY_NAME = "cluster";

    private final ClusterRepository clusterRepository;

    private final ClusterSearchRepository clusterSearchRepository;

    public ClusterResource(ClusterRepository clusterRepository, ClusterSearchRepository clusterSearchRepository) {
        this.clusterRepository = clusterRepository;
        this.clusterSearchRepository = clusterSearchRepository;
    }

    /**
     * POST  /clusters : Create a new cluster.
     *
     * @param cluster the cluster to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cluster, or with status 400 (Bad Request) if the cluster has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clusters")
    @Timed
    public ResponseEntity<Cluster> createCluster(@RequestBody Cluster cluster) throws URISyntaxException {
        log.debug("REST request to save Cluster : {}", cluster);
        if (cluster.getId() != null) {
            throw new BadRequestAlertException("A new cluster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cluster result = clusterRepository.save(cluster);
        clusterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clusters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clusters : Updates an existing cluster.
     *
     * @param cluster the cluster to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cluster,
     * or with status 400 (Bad Request) if the cluster is not valid,
     * or with status 500 (Internal Server Error) if the cluster couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clusters")
    @Timed
    public ResponseEntity<Cluster> updateCluster(@RequestBody Cluster cluster) throws URISyntaxException {
        log.debug("REST request to update Cluster : {}", cluster);
        if (cluster.getId() == null) {
            return createCluster(cluster);
        }
        Cluster result = clusterRepository.save(cluster);
        clusterSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cluster.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clusters : get all the clusters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clusters in body
     */
    @GetMapping("/clusters")
    @Timed
    public ResponseEntity<List<Cluster>> getAllClusters(Pageable pageable) {
        log.debug("REST request to get a page of Clusters");
        Page<Cluster> page = clusterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clusters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clusters/:id : get the "id" cluster.
     *
     * @param id the id of the cluster to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cluster, or with status 404 (Not Found)
     */
    @GetMapping("/clusters/{id}")
    @Timed
    public ResponseEntity<Cluster> getCluster(@PathVariable Long id) {
        log.debug("REST request to get Cluster : {}", id);
        Cluster cluster = clusterRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cluster));
    }

    /**
     * DELETE  /clusters/:id : delete the "id" cluster.
     *
     * @param id the id of the cluster to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clusters/{id}")
    @Timed
    public ResponseEntity<Void> deleteCluster(@PathVariable Long id) {
        log.debug("REST request to delete Cluster : {}", id);
        clusterRepository.delete(id);
        clusterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/clusters?query=:query : search for the cluster corresponding
     * to the query.
     *
     * @param query the query of the cluster search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/clusters")
    @Timed
    public ResponseEntity<List<Cluster>> searchClusters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Clusters for query {}", query);
        Page<Cluster> page = clusterSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/clusters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
