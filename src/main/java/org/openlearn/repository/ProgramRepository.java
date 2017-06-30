package org.openlearn.repository;

import org.openlearn.domain.Program;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Program entity.
 */
@SuppressWarnings("unused")
public interface ProgramRepository extends JpaRepository<Program,Long> {

}
