package org.openlearn.repository;

import org.openlearn.domain.School;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the School entity.
 */
@SuppressWarnings("unused")
public interface SchoolRepository extends JpaRepository<School,Long> {

}
