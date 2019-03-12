package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Teacher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    @Query("select t from Teacher t where lower(firstName) like (:searchTerm) or lower(lastName) like (:searchTerm) ")
    Page<Teacher> findAllWithSearchTerm(Pageable var1, @Param("searchTerm")String searchTerm);

    @EntityGraph(attributePaths = "mlcClasses")
    @Query("select t from Teacher t where t.id = (:id) ")
    Teacher getTeacherWithClasses(@Param("id")Long id);
}
