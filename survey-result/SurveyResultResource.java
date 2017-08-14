package com.klikpeta.suko.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.klikpeta.suko.security.annotation.MustHaveTenant;
import com.klikpeta.suko.service.SurveyResultService;
import com.klikpeta.suko.web.rest.util.HeaderUtil;
import com.klikpeta.suko.web.rest.util.PaginationUtil;
import com.klikpeta.suko.service.dto.SurveyResultDTO;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SurveyResult.
 */
@RestController
@RequestMapping("/api")
@MustHaveTenant
public class SurveyResultResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResultResource.class);
        
    @Inject
    private SurveyResultService surveyResultService;

    /**
     * POST  /survey-results : Create a new surveyResult.
     *
     * @param surveyResultDTO the surveyResultDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new surveyResultDTO, or with status 400 (Bad Request) if the surveyResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/survey-results")
    @Timed
    public ResponseEntity<SurveyResultDTO> createSurveyResult(@RequestBody SurveyResultDTO surveyResultDTO) throws URISyntaxException {
        log.debug("REST request to save SurveyResult : {}", surveyResultDTO);
        if (surveyResultDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("surveyResult", "idexists", "A new surveyResult cannot already have an ID")).body(null);
        }
        SurveyResultDTO result = surveyResultService.save(surveyResultDTO);
        return ResponseEntity.created(new URI("/api/survey-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("surveyResult", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /survey-results : Updates an existing surveyResult.
     *
     * @param surveyResultDTO the surveyResultDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated surveyResultDTO,
     * or with status 400 (Bad Request) if the surveyResultDTO is not valid,
     * or with status 500 (Internal Server Error) if the surveyResultDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/survey-results")
    @Timed
    public ResponseEntity<SurveyResultDTO> updateSurveyResult(@RequestBody SurveyResultDTO surveyResultDTO) throws URISyntaxException {
        log.debug("REST request to update SurveyResult : {}", surveyResultDTO);
        if (surveyResultDTO.getId() == null) {
            return createSurveyResult(surveyResultDTO);
        }
        SurveyResultDTO result = surveyResultService.save(surveyResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("surveyResult", surveyResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /survey-results : get all the surveyResults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of surveyResults in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/survey-results")
    @Timed
    public ResponseEntity<List<SurveyResultDTO>> getAllSurveyResults(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SurveyResults");
        Page<SurveyResultDTO> page = surveyResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/survey-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /survey-results/:id : get the "id" surveyResult.
     *
     * @param id the id of the surveyResultDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the surveyResultDTO, or with status 404 (Not Found)
     */
    @GetMapping("/survey-results/{id}")
    @Timed
    public ResponseEntity<SurveyResultDTO> getSurveyResult(@PathVariable Long id) {
        log.debug("REST request to get SurveyResult : {}", id);
        SurveyResultDTO surveyResultDTO = surveyResultService.findOne(id);
        return Optional.ofNullable(surveyResultDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /survey-results/:id : delete the "id" surveyResult.
     *
     * @param id the id of the surveyResultDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/survey-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteSurveyResult(@PathVariable Long id) {
        log.debug("REST request to delete SurveyResult : {}", id);
        surveyResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("surveyResult", id.toString())).build();
    }

    /**
     * SEARCH  /_search/survey-results?query=:query : search for the surveyResult corresponding
     * to the query.
     *
     * @param query the query of the surveyResult search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/survey-results")
    @Timed
    public ResponseEntity<List<SurveyResultDTO>> searchSurveyResults(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SurveyResults for query {}", query);
        Page<SurveyResultDTO> page = surveyResultService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/survey-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
