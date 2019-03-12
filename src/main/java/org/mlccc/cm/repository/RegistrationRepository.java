package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Registration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    
    @Query("SELECT r FROM Registration r WHERE r.student.id = (:studentId)")
    List<Registration> findAllWithStudentId(@Param("studentId") Long studentId);

    @Query("SELECT r FROM Registration r WHERE r.mlcClass.id = (:mlcClassId)")
    List<Registration> findAllWithClassId(@Param("mlcClassId") Long mlcClassId);

    @Query("SELECT r FROM Registration r WHERE r.student.id = (:studentId) AND r.mlcClass.id = (:mlcClassId)")
    List<Registration> findAllWithStudentIdClassId(@Param("studentId") Long studentId, @Param("mlcClassId") Long mlcClassId);

    @Query("SELECT r FROM Registration r join r.student.associatedAccounts sa WHERE sa.id = (:userId)")
    Page<Registration> findAllWithAssociatedUserId(Pageable var1, @Param("userId") Long userId);

    @Query("SELECT r FROM Registration r join r.mlcClass c join c.teacher t join t.account cta WHERE cta.id = (:userId)")
    Page<Registration> findAllWithTeacherUserId(Pageable var1, @Param("userId") Long userId);

    @Query("SELECT count(r.id) FROM Registration r WHERE r.mlcClass.id = (:mlcClassId)")
    Long findNumberOfRegistrationWithClassId(@Param("mlcClassId") Long mlcClassId);

}
