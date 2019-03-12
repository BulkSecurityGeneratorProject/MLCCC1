package org.mlccc.cm.service;

import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.domain.Teacher;
import org.mlccc.cm.service.dto.TeacherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Teacher.
 */
public interface TeacherService {

    /**
     * Save a teacher.
     *
     * @param teacher the entity to save
     * @return the persisted entity
     */
    Teacher save(Teacher teacher);

    /**
     *  Get all the teachers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TeacherDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" teacher.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Teacher findOne(Long id);

    /**
     *  Delete the "id" teacher.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Page<TeacherDTO> findAllWithSearchTerm(Pageable pageable, String searchTerm);

    Teacher getTeacherWithClasses(Long id);
}
