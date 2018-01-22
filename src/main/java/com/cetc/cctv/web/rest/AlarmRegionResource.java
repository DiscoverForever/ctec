package com.cetc.cctv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cetc.cctv.domain.AlarmRegion;

import com.cetc.cctv.repository.AlarmRegionRepository;
import com.cetc.cctv.repository.search.AlarmRegionSearchRepository;
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
 * REST controller for managing AlarmRegion.
 */
@RestController
@RequestMapping("/api")
public class AlarmRegionResource {

    private final Logger log = LoggerFactory.getLogger(AlarmRegionResource.class);

    private static final String ENTITY_NAME = "alarmRegion";

    private final AlarmRegionRepository alarmRegionRepository;

    private final AlarmRegionSearchRepository alarmRegionSearchRepository;

    public AlarmRegionResource(AlarmRegionRepository alarmRegionRepository, AlarmRegionSearchRepository alarmRegionSearchRepository) {
        this.alarmRegionRepository = alarmRegionRepository;
        this.alarmRegionSearchRepository = alarmRegionSearchRepository;
    }

    /**
     * POST  /alarm-regions : Create a new alarmRegion.
     *
     * @param alarmRegion the alarmRegion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alarmRegion, or with status 400 (Bad Request) if the alarmRegion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alarm-regions")
    @Timed
    public ResponseEntity<AlarmRegion> createAlarmRegion(@RequestBody AlarmRegion alarmRegion) throws URISyntaxException {
        log.debug("REST request to save AlarmRegion : {}", alarmRegion);
        if (alarmRegion.getId() != null) {
            throw new BadRequestAlertException("A new alarmRegion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlarmRegion result = alarmRegionRepository.save(alarmRegion);
        alarmRegionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/alarm-regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alarm-regions : Updates an existing alarmRegion.
     *
     * @param alarmRegion the alarmRegion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alarmRegion,
     * or with status 400 (Bad Request) if the alarmRegion is not valid,
     * or with status 500 (Internal Server Error) if the alarmRegion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alarm-regions")
    @Timed
    public ResponseEntity<AlarmRegion> updateAlarmRegion(@RequestBody AlarmRegion alarmRegion) throws URISyntaxException {
        log.debug("REST request to update AlarmRegion : {}", alarmRegion);
        if (alarmRegion.getId() == null) {
            return createAlarmRegion(alarmRegion);
        }
        AlarmRegion result = alarmRegionRepository.save(alarmRegion);
        alarmRegionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alarmRegion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alarm-regions : get all the alarmRegions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of alarmRegions in body
     */
    @GetMapping("/alarm-regions")
    @Timed
    public ResponseEntity<List<AlarmRegion>> getAllAlarmRegions(Pageable pageable) {
        log.debug("REST request to get a page of AlarmRegions");
        Page<AlarmRegion> page = alarmRegionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alarm-regions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alarm-regions/:id : get the "id" alarmRegion.
     *
     * @param id the id of the alarmRegion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alarmRegion, or with status 404 (Not Found)
     */
    @GetMapping("/alarm-regions/{id}")
    @Timed
    public ResponseEntity<AlarmRegion> getAlarmRegion(@PathVariable Long id) {
        log.debug("REST request to get AlarmRegion : {}", id);
        AlarmRegion alarmRegion = alarmRegionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(alarmRegion));
    }

    /**
     * DELETE  /alarm-regions/:id : delete the "id" alarmRegion.
     *
     * @param id the id of the alarmRegion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alarm-regions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlarmRegion(@PathVariable Long id) {
        log.debug("REST request to delete AlarmRegion : {}", id);
        alarmRegionRepository.delete(id);
        alarmRegionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/alarm-regions?query=:query : search for the alarmRegion corresponding
     * to the query.
     *
     * @param query the query of the alarmRegion search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/alarm-regions")
    @Timed
    public ResponseEntity<List<AlarmRegion>> searchAlarmRegions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AlarmRegions for query {}", query);
        Page<AlarmRegion> page = alarmRegionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/alarm-regions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
