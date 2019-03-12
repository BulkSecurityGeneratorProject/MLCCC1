package org.mlccc.cm.service;

import org.mlccc.cm.domain.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Registration.
 */
public interface RegistrationService {

    /**
     * Save a registration.
     *
     * @param registration the entity to save
     * @return the persisted entity
     */
    Registration save(Registration registration);

    /**
     *  Get all the registrations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Registration> findAll(Pageable pageable);

    /**
     *  Get the "id" registration.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Registration findOne(Long id);


    List<Registration> findAllWithStudentIdClassId(Long studentId, Long mlcClassId);

    List<Registration> findAllWithStudentId(Long studentId);

    List<Registration> findAllWithClassId(Long mlcClassId);

    Page<Registration> findAllWithAssociatedUserId(Pageable pageable, Long userId);

    Page<Registration> findAllWithTeacherUserId(Pageable pageable, Long userId);

    Long findNumberOfRegistrationWithClassId(Long classId);
    /**
     *  Delete the "id" registration.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
