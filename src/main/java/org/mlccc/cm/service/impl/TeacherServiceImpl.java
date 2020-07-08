package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.TeacherService;
import org.mlccc.cm.domain.Teacher;
import org.mlccc.cm.repository.TeacherRepository;
import org.mlccc.cm.service.dto.TeacherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Teacher.
 */
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService{

    private final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * Save a teacher.
     *
     * @param teacher the entity to save
     * @return the persisted entity
     */
    @Override
    public Teacher save(Teacher teacher) {
        log.debug("Request to save Teacher : {}", teacher);
        return teacherRepository.save(teacher);
    }

    /**
     *  Get all the teachers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeacherDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Teachers");
        return teacherRepository.findAll(pageable).map(TeacherDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findAll() {
        log.debug("Request to get all Teachers");
        return teacherRepository.findAll();
    }

    /**
     *  Get one teacher by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Teacher findOne(Long id) {
        log.debug("Request to get Teacher : {}", id);
        return teacherRepository.findOne(id);
    }

    /**
     *  Delete the  teacher by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Teacher : {}", id);
        teacherRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherDTO> findAllWithSearchTerm(Pageable pageable, String searchTerm) {
        log.debug("Request to get all Teachers with searchTerm: {}", searchTerm);
        return teacherRepository.findAllWithSearchTerm(pageable, searchTerm.toLowerCase()+"%").map(TeacherDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherWithClasses(Long id) {
        log.debug("Request to get teacher with classes: {}", id);
        return teacherRepository.getTeacherWithClasses(id);
    }
}
