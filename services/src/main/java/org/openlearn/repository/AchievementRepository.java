package org.openlearn.repository;

import org.openlearn.domain.Achievement;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Achievement entity.
 */
@SuppressWarnings("unused")
public interface AchievementRepository extends JpaRepository<Achievement,Long> {

}
