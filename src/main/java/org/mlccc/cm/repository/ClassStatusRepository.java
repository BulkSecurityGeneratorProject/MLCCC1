package org.mlccc.cm.repository;

import org.mlccc.cm.domain.ClassStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClassStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassStatusRepository extends JpaRepository<ClassStatus,Long> {
    @Query("SELECT cs FROM ClassStatus cs WHERE cs.status = (:status) ")
    ClassStatus findByName(@Param("status") String status);
}
