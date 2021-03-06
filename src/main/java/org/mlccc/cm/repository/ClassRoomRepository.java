package org.mlccc.cm.repository;

import org.mlccc.cm.domain.ClassRoom;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClassRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {

}
