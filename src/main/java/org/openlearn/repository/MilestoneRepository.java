package org.openlearn.repository;

import org.openlearn.domain.Milestone;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Milestone entity.
 */
@SuppressWarnings("unused")
public interface MilestoneRepository extends JpaRepository<Milestone,Long> {

}
