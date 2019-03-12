package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.RegistrationService;
import org.mlccc.cm.domain.Registration;
import org.mlccc.cm.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Registration.
 */
@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService{

    private final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final RegistrationRepository registrationRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    /**
     * Save a registration.
     *
     * @param registration the entity to save
     * @return the persisted entity
     */
    @Override
    public Registration save(Registration registration) {
        log.debug("Request to save Registration : {}", registration);
        return registrationRepository.save(registration);
    }


    /**
     *  Get all the registrations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Registration> findAll(Pageable pageable) {
        log.debug("Request to get all Registrations");
        return registrationRepository.findAll(pageable);
    }

    /**
     *  Get one registration by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Registration findOne(Long id) {
        log.debug("Request to get Registration : {}", id);
        return registrationRepository.findOne(id);
    }

    /**
     *  Delete the  registration by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Registration : {}", id);
        registrationRepository.delete(id);
    }

    @Override
    public List<Registration> findAllWithStudentIdClassId(Long studentId, Long mlcClassId){
        log.debug("Request to findAllWithStudentIdClassId: {} {} ", studentId, mlcClassId);
        return registrationRepository.findAllWithStudentIdClassId(studentId, mlcClassId);
    }

    @Override
    public List<Registration> findAllWithStudentId(Long studentId){
        log.debug("Request to findAllWithStudentId: {}", studentId);
        return registrationRepository.findAllWithStudentId(studentId);
    }

    @Override
    public List<Registration> findAllWithClassId(Long mlcClassId){
        log.debug("Request to findAllWithClassId: {}", mlcClassId);
        return registrationRepository.findAllWithClassId(mlcClassId);
    }

    @Override
    public Page<Registration> findAllWithAssociatedUserId(Pageable pageable, Long userId){
        log.debug("Request to findAllWithAssociatedUserId: {}", userId);
        return registrationRepository.findAllWithAssociatedUserId(pageable, userId);
    }

    @Override
    public Page<Registration> findAllWithTeacherUserId(Pageable pageable, Long userId){
        log.debug("Request to findAllWithTeacherUserId: {}", userId);
        return registrationRepository.findAllWithTeacherUserId(pageable, userId);
    }

    @Override
    public Long findNumberOfRegistrationWithClassId(Long classId){
        log.debug("Request to findNumberOfRegistrationWithClassId: {}", classId);
        return registrationRepository.findNumberOfRegistrationWithClassId(classId);
    }


}
