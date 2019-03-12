package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.NewsLetter;
import org.mlccc.cm.service.NewsLetterService;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing NewsLetter.
 */
@RestController
@RequestMapping("/api")
public class NewsLetterResource {

    private final Logger log = LoggerFactory.getLogger(NewsLetterResource.class);

    private static final String ENTITY_NAME = "newsLetter";

    private final NewsLetterService newsLetterService;

    public NewsLetterResource(NewsLetterService newsLetterService) {
        this.newsLetterService = newsLetterService;
    }

    /**
     * POST  /news-letters : Create a new newsLetter.
     *
     * @param newsLetter the newsLetter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new newsLetter, or with status 400 (Bad Request) if the newsLetter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/news-letters")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<NewsLetter> createNewsLetter(@RequestBody NewsLetter newsLetter) throws URISyntaxException {
        log.debug("REST request to save NewsLetter : {}", newsLetter);
        if (newsLetter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new newsLetter cannot already have an ID")).body(null);
        }
        NewsLetter result = newsLetterService.save(newsLetter);
        return ResponseEntity.created(new URI("/api/news-letters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /news-letters : Updates an existing newsLetter.
     *
     * @param newsLetter the newsLetter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated newsLetter,
     * or with status 400 (Bad Request) if the newsLetter is not valid,
     * or with status 500 (Internal Server Error) if the newsLetter couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/news-letters")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<NewsLetter> updateNewsLetter(@RequestBody NewsLetter newsLetter) throws URISyntaxException {
        log.debug("REST request to update NewsLetter : {}", newsLetter);
        if (newsLetter.getId() == null) {
            return createNewsLetter(newsLetter);
        }
        NewsLetter result = newsLetterService.save(newsLetter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, newsLetter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /news-letters : get all the newsLetters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of newsLetters in body
     */
    @GetMapping("/news-letters")
    @Timed
    public List<NewsLetter> getAllNewsLetters() {
        log.debug("REST request to get all NewsLetters");
        return newsLetterService.findAll();
    }

    /**
     * GET  /news-letters/:id : get the "id" newsLetter.
     *
     * @param id the id of the newsLetter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the newsLetter, or with status 404 (Not Found)
     */
    @GetMapping("/news-letters/{id}")
    @Timed
    public ResponseEntity<NewsLetter> getNewsLetter(@PathVariable Long id) {
        log.debug("REST request to get NewsLetter : {}", id);
        NewsLetter newsLetter = newsLetterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(newsLetter));
    }

    /**
     * DELETE  /news-letters/:id : delete the "id" newsLetter.
     *
     * @param id the id of the newsLetter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/news-letters/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteNewsLetter(@PathVariable Long id) {
        log.debug("REST request to delete NewsLetter : {}", id);
        newsLetterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
