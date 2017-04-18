package org.opengive.denver.stem.repository;

import org.opengive.denver.stem.domain.Milestone;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Milestone entity.
 */
@SuppressWarnings("unused")
public interface MilestoneRepository extends JpaRepository<Milestone,Long> {

}
