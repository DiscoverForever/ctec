package com.cetc.cctv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cetc.cctv.domain.AlarmHistory;

import com.cetc.cctv.repository.AlarmHistoryRepository;
import com.cetc.cctv.repository.search.AlarmHistorySearchRepository;
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
 * REST controller for managing AlarmHistory.
 */
@RestController
@RequestMapping("/api")
public class AlarmHistoryResource {

    private final Logger log = LoggerFactory.getLogger(AlarmHistoryResource.class);

    private static final String ENTITY_NAME = "alarmHistory";

    private final AlarmHistoryRepository alarmHistoryRepository;

    private final AlarmHistorySearchRepository alarmHistorySearchRepository;

    public AlarmHistoryResource(AlarmHistoryRepository alarmHistoryRepository, AlarmHistorySearchRepository alarmHistorySearchRepository) {
        this.alarmHistoryRepository = alarmHistoryRepository;
        this.alarmHistorySearchRepository = alarmHistorySearchRepository;
    }

    /**
     * POST  /alarm-histories : Create a new alarmHistory.
     *
     * @param alarmHistory the alarmHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alarmHistory, or with status 400 (Bad Request) if the alarmHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alarm-histories")
    @Timed
    public ResponseEntity<AlarmHistory> createAlarmHistory(@RequestBody AlarmHistory alarmHistory) throws URISyntaxException {
        log.debug("REST request to save AlarmHistory : {}", alarmHistory);
        if (alarmHistory.getId() != null) {
            throw new BadRequestAlertException("A new alarmHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlarmHistory result = alarmHistoryRepository.save(alarmHistory);
        alarmHistorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/alarm-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alarm-histories : Updates an existing alarmHistory.
     *
     * @param alarmHistory the alarmHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alarmHistory,
     * or with status 400 (Bad Request) if the alarmHistory is not valid,
     * or with status 500 (Internal Server Error) if the alarmHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alarm-histories")
    @Timed
    public ResponseEntity<AlarmHistory> updateAlarmHistory(@RequestBody AlarmHistory alarmHistory) throws URISyntaxException {
        log.debug("REST request to update AlarmHistory : {}", alarmHistory);
        if (alarmHistory.getId() == null) {
            return createAlarmHistory(alarmHistory);
        }
        AlarmHistory result = alarmHistoryRepository.save(alarmHistory);
        alarmHistorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alarmHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alarm-histories : get all the alarmHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of alarmHistories in body
     */
    @GetMapping("/alarm-histories")
    @Timed
    public ResponseEntity<List<AlarmHistory>> getAllAlarmHistories(Pageable pageable) {
        log.debug("REST request to get a page of AlarmHistories");
        Page<AlarmHistory> page = alarmHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alarm-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alarm-histories/:id : get the "id" alarmHistory.
     *
     * @param id the id of the alarmHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alarmHistory, or with status 404 (Not Found)
     */
    @GetMapping("/alarm-histories/{id}")
    @Timed
    public ResponseEntity<AlarmHistory> getAlarmHistory(@PathVariable Long id) {
        log.debug("REST request to get AlarmHistory : {}", id);
        AlarmHistory alarmHistory = alarmHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(alarmHistory));
    }

    /**
     * DELETE  /alarm-histories/:id : delete the "id" alarmHistory.
     *
     * @param id the id of the alarmHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alarm-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlarmHistory(@PathVariable Long id) {
        log.debug("REST request to delete AlarmHistory : {}", id);
        alarmHistoryRepository.delete(id);
        alarmHistorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/alarm-histories?query=:query : search for the alarmHistory corresponding
     * to the query.
     *
     * @param query the query of the alarmHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/alarm-histories")
    @Timed
    public ResponseEntity<List<AlarmHistory>> searchAlarmHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AlarmHistories for query {}", query);
        Page<AlarmHistory> page = alarmHistorySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/alarm-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
