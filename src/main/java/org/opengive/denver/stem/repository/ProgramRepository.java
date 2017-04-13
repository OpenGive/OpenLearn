package org.opengive.denver.stem.repository;

import org.opengive.denver.stem.domain.Program;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Program entity.
 */
@SuppressWarnings("unused")
public interface ProgramRepository extends JpaRepository<Program,Long> {

}
