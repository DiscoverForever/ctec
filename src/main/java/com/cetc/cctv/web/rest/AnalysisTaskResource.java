package com.cetc.cctv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cetc.cctv.domain.AnalysisTask;

import com.cetc.cctv.repository.AnalysisTaskRepository;
import com.cetc.cctv.repository.search.AnalysisTaskSearchRepository;
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
 * REST controller for managing AnalysisTask.
 */
@RestController
@RequestMapping("/api")
public class AnalysisTaskResource {

    private final Logger log = LoggerFactory.getLogger(AnalysisTaskResource.class);

    private static final String ENTITY_NAME = "analysisTask";

    private final AnalysisTaskRepository analysisTaskRepository;

    private final AnalysisTaskSearchRepository analysisTaskSearchRepository;

    public AnalysisTaskResource(AnalysisTaskRepository analysisTaskRepository, AnalysisTaskSearchRepository analysisTaskSearchRepository) {
        this.analysisTaskRepository = analysisTaskRepository;
        this.analysisTaskSearchRepository = analysisTaskSearchRepository;
    }

    /**
     * POST  /analysis-tasks : Create a new analysisTask.
     *
     * @param analysisTask the analysisTask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analysisTask, or with status 400 (Bad Request) if the analysisTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/analysis-tasks")
    @Timed
    public ResponseEntity<AnalysisTask> createAnalysisTask(@RequestBody AnalysisTask analysisTask) throws URISyntaxException {
        log.debug("REST request to save AnalysisTask : {}", analysisTask);
        if (analysisTask.getId() != null) {
            throw new BadRequestAlertException("A new analysisTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalysisTask result = analysisTaskRepository.save(analysisTask);
        analysisTaskSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/analysis-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analysis-tasks : Updates an existing analysisTask.
     *
     * @param analysisTask the analysisTask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analysisTask,
     * or with status 400 (Bad Request) if the analysisTask is not valid,
     * or with status 500 (Internal Server Error) if the analysisTask couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/analysis-tasks")
    @Timed
    public ResponseEntity<AnalysisTask> updateAnalysisTask(@RequestBody AnalysisTask analysisTask) throws URISyntaxException {
        log.debug("REST request to update AnalysisTask : {}", analysisTask);
        if (analysisTask.getId() == null) {
            return createAnalysisTask(analysisTask);
        }
        AnalysisTask result = analysisTaskRepository.save(analysisTask);
        analysisTaskSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analysisTask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analysis-tasks : get all the analysisTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of analysisTasks in body
     */
    @GetMapping("/analysis-tasks")
    @Timed
    public ResponseEntity<List<AnalysisTask>> getAllAnalysisTasks(Pageable pageable) {
        log.debug("REST request to get a page of AnalysisTasks");
        Page<AnalysisTask> page = analysisTaskRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analysis-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /analysis-tasks/:id : get the "id" analysisTask.
     *
     * @param id the id of the analysisTask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analysisTask, or with status 404 (Not Found)
     */
    @GetMapping("/analysis-tasks/{id}")
    @Timed
    public ResponseEntity<AnalysisTask> getAnalysisTask(@PathVariable Long id) {
        log.debug("REST request to get AnalysisTask : {}", id);
        AnalysisTask analysisTask = analysisTaskRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(analysisTask));
    }

    /**
     * DELETE  /analysis-tasks/:id : delete the "id" analysisTask.
     *
     * @param id the id of the analysisTask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analysis-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnalysisTask(@PathVariable Long id) {
        log.debug("REST request to delete AnalysisTask : {}", id);
        analysisTaskRepository.delete(id);
        analysisTaskSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/analysis-tasks?query=:query : search for the analysisTask corresponding
     * to the query.
     *
     * @param query the query of the analysisTask search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/analysis-tasks")
    @Timed
    public ResponseEntity<List<AnalysisTask>> searchAnalysisTasks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalysisTasks for query {}", query);
        Page<AnalysisTask> page = analysisTaskSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/analysis-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
