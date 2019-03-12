package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.domain.Teacher;
import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.service.TeacherService;
import org.mlccc.cm.service.dto.TeacherDTO;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import org.mlccc.cm.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Teacher.
 */
@RestController
@RequestMapping("/api")
public class TeacherResource {

    private final Logger log = LoggerFactory.getLogger(TeacherResource.class);

    private static final String ENTITY_NAME = "teacher";

    private final TeacherService teacherService;

    private final MlcClassService classService;

    public TeacherResource(TeacherService teacherService, MlcClassService classService) {
        this.teacherService = teacherService;
        this.classService = classService;
    }

    /**
     * POST  /teachers : Create a new teacher.
     *
     * @param teacher the teacher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teacher, or with status 400 (Bad Request) if the teacher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/teachers")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) throws URISyntaxException {
        log.debug("REST request to save Teacher : {}", teacher);
        if (teacher.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new teacher cannot already have an ID")).body(null);
        }
        Teacher result = teacherService.save(teacher);
        return ResponseEntity.created(new URI("/api/teachers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teachers : Updates an existing teacher.
     *
     * @param teacher the teacher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teacher,
     * or with status 400 (Bad Request) if the teacher is not valid,
     * or with status 500 (Internal Server Error) if the teacher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/teachers")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Teacher> updateTeacher(@RequestBody Teacher teacher) throws URISyntaxException {
        log.debug("REST request to update Teacher : {}", teacher);
        if (teacher.getId() == null) {
            return createTeacher(teacher);
        }
        Teacher result = teacherService.save(teacher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teacher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /teachers : get all the teachers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of teachers in body
     */
    @GetMapping("/teachers")
    @Timed
    public ResponseEntity<List<TeacherDTO>> getAllTeachers(@ApiParam Pageable pageable, @ApiParam String searchTerm) {
        log.debug("REST request to get a page of Teachers");
        Page<TeacherDTO> page = null;
        if(StringUtils.isEmpty(searchTerm)) {
            page = teacherService.findAll(pageable);
        } else {
            page = teacherService.findAllWithSearchTerm(pageable, searchTerm);
        }
        page.forEach(teacherDto -> {
            Set<String> categories = new HashSet<>();
            Set<String> classesString = new HashSet<>();
            List<MlcClass> classes =  classService.findAllWithTeacherId(teacherDto.getId());
            classes.forEach(mlcClass ->{
                if(mlcClass.getMlcClassCategory() != null){
                    categories.add(mlcClass.getMlcClassCategory().getName());
                }
                classesString.add(mlcClass.getClassName());
            });
            teacherDto.setClassCategories(categories.toString().replaceAll("\\[","").replaceAll("]",""));
            teacherDto.setClasses(classesString.toString().replaceAll("\\[","").replaceAll("]",""));
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/teachers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /teachers/:id : get the "id" teacher.
     *
     * @param id the id of the teacher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teacher, or with status 404 (Not Found)
     */
    @GetMapping("/teachers/{id}")
    @Timed
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable Long id) {
        log.debug("REST request to get Teacher : {}", id);
        Teacher teacher = teacherService.getTeacherWithClasses(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(new TeacherDTO(teacher)));
    }

    /**
     * DELETE  /teachers/:id : delete the "id" teacher.
     *
     * @param id the id of the teacher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/teachers/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        log.debug("REST request to delete Teacher : {}", id);
        teacherService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
