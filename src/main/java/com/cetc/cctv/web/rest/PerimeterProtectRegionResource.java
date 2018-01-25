package com.cetc.cctv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cetc.cctv.domain.PerimeterProtectRegion;

import com.cetc.cctv.repository.PerimeterProtectRegionRepository;
import com.cetc.cctv.repository.search.PerimeterProtectRegionSearchRepository;
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
 * REST controller for managing PerimeterProtectRegion.
 */
@RestController
@RequestMapping("/api")
public class PerimeterProtectRegionResource {

    private final Logger log = LoggerFactory.getLogger(PerimeterProtectRegionResource.class);

    private static final String ENTITY_NAME = "perimeterProtectRegion";

    private final PerimeterProtectRegionRepository perimeterProtectRegionRepository;

    private final PerimeterProtectRegionSearchRepository perimeterProtectRegionSearchRepository;

    public PerimeterProtectRegionResource(PerimeterProtectRegionRepository perimeterProtectRegionRepository, PerimeterProtectRegionSearchRepository perimeterProtectRegionSearchRepository) {
        this.perimeterProtectRegionRepository = perimeterProtectRegionRepository;
        this.perimeterProtectRegionSearchRepository = perimeterProtectRegionSearchRepository;
    }

    /**
     * POST  /perimeter-protect-regions : Create a new perimeterProtectRegion.
     *
     * @param perimeterProtectRegion the perimeterProtectRegion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new perimeterProtectRegion, or with status 400 (Bad Request) if the perimeterProtectRegion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/perimeter-protect-regions")
    @Timed
    public ResponseEntity<PerimeterProtectRegion> createPerimeterProtectRegion(@RequestBody PerimeterProtectRegion perimeterProtectRegion) throws URISyntaxException {
        log.debug("REST request to save PerimeterProtectRegion : {}", perimeterProtectRegion);
        if (perimeterProtectRegion.getId() != null) {
            throw new BadRequestAlertException("A new perimeterProtectRegion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerimeterProtectRegion result = perimeterProtectRegionRepository.save(perimeterProtectRegion);
        perimeterProtectRegionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/perimeter-protect-regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /perimeter-protect-regions : Updates an existing perimeterProtectRegion.
     *
     * @param perimeterProtectRegion the perimeterProtectRegion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated perimeterProtectRegion,
     * or with status 400 (Bad Request) if the perimeterProtectRegion is not valid,
     * or with status 500 (Internal Server Error) if the perimeterProtectRegion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/perimeter-protect-regions")
    @Timed
    public ResponseEntity<PerimeterProtectRegion> updatePerimeterProtectRegion(@RequestBody PerimeterProtectRegion perimeterProtectRegion) throws URISyntaxException {
        log.debug("REST request to update PerimeterProtectRegion : {}", perimeterProtectRegion);
        if (perimeterProtectRegion.getId() == null) {
            return createPerimeterProtectRegion(perimeterProtectRegion);
        }
        PerimeterProtectRegion result = perimeterProtectRegionRepository.save(perimeterProtectRegion);
        perimeterProtectRegionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, perimeterProtectRegion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /perimeter-protect-regions : get all the perimeterProtectRegions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of perimeterProtectRegions in body
     */
    @GetMapping("/perimeter-protect-regions")
    @Timed
    public ResponseEntity<List<PerimeterProtectRegion>> getAllPerimeterProtectRegions(Pageable pageable) {
        log.debug("REST request to get a page of PerimeterProtectRegions");
        Page<PerimeterProtectRegion> page = perimeterProtectRegionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/perimeter-protect-regions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /perimeter-protect-regions/:id : get the "id" perimeterProtectRegion.
     *
     * @param id the id of the perimeterProtectRegion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the perimeterProtectRegion, or with status 404 (Not Found)
     */
    @GetMapping("/perimeter-protect-regions/{id}")
    @Timed
    public ResponseEntity<PerimeterProtectRegion> getPerimeterProtectRegion(@PathVariable Long id) {
        log.debug("REST request to get PerimeterProtectRegion : {}", id);
        PerimeterProtectRegion perimeterProtectRegion = perimeterProtectRegionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(perimeterProtectRegion));
    }

    /**
     * DELETE  /perimeter-protect-regions/:id : delete the "id" perimeterProtectRegion.
     *
     * @param id the id of the perimeterProtectRegion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/perimeter-protect-regions/{id}")
    @Timed
    public ResponseEntity<Void> deletePerimeterProtectRegion(@PathVariable Long id) {
        log.debug("REST request to delete PerimeterProtectRegion : {}", id);
        perimeterProtectRegionRepository.delete(id);
        perimeterProtectRegionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/perimeter-protect-regions?query=:query : search for the perimeterProtectRegion corresponding
     * to the query.
     *
     * @param query the query of the perimeterProtectRegion search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/perimeter-protect-regions")
    @Timed
    public ResponseEntity<List<PerimeterProtectRegion>> searchPerimeterProtectRegions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PerimeterProtectRegions for query {}", query);
        Page<PerimeterProtectRegion> page = perimeterProtectRegionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/perimeter-protect-regions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
