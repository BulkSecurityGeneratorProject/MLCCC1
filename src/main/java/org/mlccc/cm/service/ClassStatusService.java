package org.mlccc.cm.service;

import org.mlccc.cm.domain.ClassStatus;
import java.util.List;

/**
 * Service Interface for managing ClassStatus.
 */
public interface ClassStatusService {

    /**
     * Save a classStatus.
     *
     * @param classStatus the entity to save
     * @return the persisted entity
     */
    ClassStatus save(ClassStatus classStatus);

    /**
     *  Get all the classStatuses.
     *
     *  @return the list of entities
     */
    List<ClassStatus> findAll();

    /**
     *  Get the "id" classStatus.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClassStatus findOne(Long id);

    ClassStatus findByName(String status);
    /**
     *  Delete the "id" classStatus.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
