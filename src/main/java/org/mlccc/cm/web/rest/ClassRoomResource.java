package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.ClassRoom;
import org.mlccc.cm.service.ClassRoomService;
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
 * REST controller for managing ClassRoom.
 */
@RestController
@RequestMapping("/api")
public class ClassRoomResource {

    private final Logger log = LoggerFactory.getLogger(ClassRoomResource.class);

    private static final String ENTITY_NAME = "classRoom";

    private final ClassRoomService classRoomService;

    public ClassRoomResource(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    /**
     * POST  /class-rooms : Create a new classRoom.
     *
     * @param classRoom the classRoom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classRoom, or with status 400 (Bad Request) if the classRoom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/class-rooms")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ClassRoom> createClassRoom(@RequestBody ClassRoom classRoom) throws URISyntaxException {
        log.debug("REST request to save ClassRoom : {}", classRoom);
        if (classRoom.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new classRoom cannot already have an ID")).body(null);
        }
        ClassRoom result = classRoomService.save(classRoom);
        return ResponseEntity.created(new URI("/api/class-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /class-rooms : Updates an existing classRoom.
     *
     * @param classRoom the classRoom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classRoom,
     * or with status 400 (Bad Request) if the classRoom is not valid,
     * or with status 500 (Internal Server Error) if the classRoom couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/class-rooms")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ClassRoom> updateClassRoom(@RequestBody ClassRoom classRoom) throws URISyntaxException {
        log.debug("REST request to update ClassRoom : {}", classRoom);
        if (classRoom.getId() == null) {
            return createClassRoom(classRoom);
        }
        ClassRoom result = classRoomService.save(classRoom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classRoom.getId().toString()))
            .body(result);
    }

    /**
     * GET  /class-rooms : get all the classRooms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of classRooms in body
     */
    @GetMapping("/class-rooms")
    @Timed
    public List<ClassRoom> getAllClassRooms() {
        log.debug("REST request to get all ClassRooms");
        return classRoomService.findAll();
    }

    /**
     * GET  /class-rooms/:id : get the "id" classRoom.
     *
     * @param id the id of the classRoom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classRoom, or with status 404 (Not Found)
     */
    @GetMapping("/class-rooms/{id}")
    @Timed
    public ResponseEntity<ClassRoom> getClassRoom(@PathVariable Long id) {
        log.debug("REST request to get ClassRoom : {}", id);
        ClassRoom classRoom = classRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(classRoom));
    }

    /**
     * DELETE  /class-rooms/:id : delete the "id" classRoom.
     *
     * @param id the id of the classRoom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/class-rooms/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteClassRoom(@PathVariable Long id) {
        log.debug("REST request to delete ClassRoom : {}", id);
        classRoomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
