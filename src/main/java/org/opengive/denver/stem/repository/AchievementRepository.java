package org.opengive.denver.stem.repository;

import org.opengive.denver.stem.domain.Achievement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Achievement entity.
 */
@SuppressWarnings("unused")
public interface AchievementRepository extends JpaRepository<Achievement,Long> {

}
