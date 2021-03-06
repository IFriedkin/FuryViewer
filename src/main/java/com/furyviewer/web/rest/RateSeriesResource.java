package com.furyviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.furyviewer.domain.RateMovie;
import com.furyviewer.domain.RateSeries;

import com.furyviewer.repository.RateMovieRepository;
import com.furyviewer.repository.RateSeriesRepository;
import com.furyviewer.repository.UserRepository;
import com.furyviewer.security.SecurityUtils;
import com.furyviewer.web.rest.errors.BadRequestAlertException;
import com.furyviewer.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingInt;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RateSeries.
 */
@RestController
@RequestMapping("/api")
public class RateSeriesResource {

    private final Logger log = LoggerFactory.getLogger(RateSeriesResource.class);

    private static final String ENTITY_NAME = "rateSeries";

    private final RateSeriesRepository rateSeriesRepository;

    public RateSeriesResource(RateSeriesRepository rateSeriesRepository) {
        this.rateSeriesRepository = rateSeriesRepository;
    }

    /**
     * POST  /rate-series : Create a new rateSeries.
     *
     * @param rateSeries the rateSeries to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rateSeries, or with status 400 (Bad Request) if the rateSeries has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rate-series")
    @Timed
    public ResponseEntity<RateSeries> createRateSeries(@RequestBody RateSeries rateSeries) throws URISyntaxException {
        log.debug("REST request to save RateSeries : {}", rateSeries);
        if (rateSeries.getId() != null) {
            throw new BadRequestAlertException("A new rateSeries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RateSeries result = rateSeriesRepository.save(rateSeries);
        return ResponseEntity.created(new URI("/api/rate-series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rate-series : Updates an existing rateSeries.
     *
     * @param rateSeries the rateSeries to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rateSeries,
     * or with status 400 (Bad Request) if the rateSeries is not valid,
     * or with status 500 (Internal Server Error) if the rateSeries couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rate-series")
    @Timed
    public ResponseEntity<RateSeries> updateRateSeries(@RequestBody RateSeries rateSeries) throws URISyntaxException {
        log.debug("REST request to update RateSeries : {}", rateSeries);
        if (rateSeries.getId() == null) {
            return createRateSeries(rateSeries);
        }
        RateSeries result = rateSeriesRepository.save(rateSeries);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rateSeries.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rate-series : get all the rateSeries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rateSeries in body
     */
    @GetMapping("/rate-series")
    @Timed
    public List<RateSeries> getAllRateSeries() {
        log.debug("REST request to get all RateSeries");
        return rateSeriesRepository.findAll();
        }

    /**
     * GET  /rate-series/:id : get the "id" rateSeries.
     *
     * @param id the id of the rateSeries to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rateSeries, or with status 404 (Not Found)
     */
    @GetMapping("/rate-series/{id}")
    @Timed
    public ResponseEntity<RateSeries> getRateSeries(@PathVariable Long id) {
        log.debug("REST request to get RateSeries : {}", id);
        RateSeries rateSeries = rateSeriesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rateSeries));
    }

    @GetMapping("/rate-series-media/{id}")
    @Timed
    public Double getRateSeriesMedia(@PathVariable Long id) {
        log.debug("REST request to get RateSeries : {}", id);
        return rateSeriesRepository.RateSeriesMedia(id);
        //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rateSeries));
    }



    /**
     * DELETE  /rate-series/:id : delete the "id" rateSeries.
     *
     * @param id the id of the rateSeries to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rate-series/{id}")
    @Timed
    public ResponseEntity<Void> deleteRateSeries(@PathVariable Long id) {
        log.debug("REST request to delete RateSeries : {}", id);
        rateSeriesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
