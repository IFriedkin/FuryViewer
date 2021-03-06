package com.furyviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.furyviewer.domain.ChapterSeen;

import com.furyviewer.repository.ChapterSeenRepository;
import com.furyviewer.web.rest.errors.BadRequestAlertException;
import com.furyviewer.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ChapterSeen.
 */
@RestController
@RequestMapping("/api")
public class ChapterSeenResource {

    private final Logger log = LoggerFactory.getLogger(ChapterSeenResource.class);

    private static final String ENTITY_NAME = "chapterSeen";

    private final ChapterSeenRepository chapterSeenRepository;

    public ChapterSeenResource(ChapterSeenRepository chapterSeenRepository) {
        this.chapterSeenRepository = chapterSeenRepository;
    }

    /**
     * POST  /chapter-seens : Create a new chapterSeen.
     *
     * @param chapterSeen the chapterSeen to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chapterSeen, or with status 400 (Bad Request) if the chapterSeen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chapter-seens")
    @Timed
    public ResponseEntity<ChapterSeen> createChapterSeen(@RequestBody ChapterSeen chapterSeen) throws URISyntaxException {
        log.debug("REST request to save ChapterSeen : {}", chapterSeen);
        if (chapterSeen.getId() != null) {
            throw new BadRequestAlertException("A new chapterSeen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChapterSeen result = chapterSeenRepository.save(chapterSeen);
        return ResponseEntity.created(new URI("/api/chapter-seens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chapter-seens : Updates an existing chapterSeen.
     *
     * @param chapterSeen the chapterSeen to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chapterSeen,
     * or with status 400 (Bad Request) if the chapterSeen is not valid,
     * or with status 500 (Internal Server Error) if the chapterSeen couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chapter-seens")
    @Timed
    public ResponseEntity<ChapterSeen> updateChapterSeen(@RequestBody ChapterSeen chapterSeen) throws URISyntaxException {
        log.debug("REST request to update ChapterSeen : {}", chapterSeen);
        if (chapterSeen.getId() == null) {
            return createChapterSeen(chapterSeen);
        }
        ChapterSeen result = chapterSeenRepository.save(chapterSeen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chapterSeen.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chapter-seens : get all the chapterSeens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chapterSeens in body
     */
    @GetMapping("/chapter-seens")
    @Timed
    public List<ChapterSeen> getAllChapterSeens() {
        log.debug("REST request to get all ChapterSeens");
        return chapterSeenRepository.findAll();
        }

    /**
     * GET  /chapter-seens/:id : get the "id" chapterSeen.
     *
     * @param id the id of the chapterSeen to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chapterSeen, or with status 404 (Not Found)
     */
    @GetMapping("/chapter-seens/{id}")
    @Timed
    public ResponseEntity<ChapterSeen> getChapterSeen(@PathVariable Long id) {
        log.debug("REST request to get ChapterSeen : {}", id);
        ChapterSeen chapterSeen = chapterSeenRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chapterSeen));
    }

    /**
     * DELETE  /chapter-seens/:id : delete the "id" chapterSeen.
     *
     * @param id the id of the chapterSeen to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chapter-seens/{id}")
    @Timed
    public ResponseEntity<Void> deleteChapterSeen(@PathVariable Long id) {
        log.debug("REST request to delete ChapterSeen : {}", id);
        chapterSeenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
