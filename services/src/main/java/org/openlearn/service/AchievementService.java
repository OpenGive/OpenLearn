package org.openlearn.service;

import org.openlearn.domain.Achievement;
import org.openlearn.repository.AchievementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Achievement.
 */
@Service
@Transactional
public class AchievementService {

	private final Logger log = LoggerFactory.getLogger(AchievementService.class);

	private final AchievementRepository achievementRepository;

	public AchievementService(final AchievementRepository achievementRepository) {
		this.achievementRepository = achievementRepository;
	}

	/**
	 * Save a achievement.
	 *
	 * @param achievement the entity to save
	 * @return the persisted entity
	 */
	public Achievement save(final Achievement achievement) {
		log.debug("Request to save Achievement : {}", achievement);
		final Achievement result = achievementRepository.save(achievement);
		return result;
	}

	/**
	 *  Get all the achievements.
	 *
	 *  @param pageable the pagination information
	 *  @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Achievement> findAll(final Pageable pageable) {
		log.debug("Request to get all Achievements");
		final Page<Achievement> result = achievementRepository.findAll(pageable);
		return result;
	}

	/**
	 *  Get one achievement by id.
	 *
	 *  @param id the id of the entity
	 *  @return the entity
	 */
	@Transactional(readOnly = true)
	public Achievement findOne(final Long id) {
		log.debug("Request to get Achievement : {}", id);
		final Achievement achievement = achievementRepository.findOne(id);
		return achievement;
	}

	/**
	 *  Delete the  achievement by id.
	 *
	 *  @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Achievement : {}", id);
		achievementRepository.delete(id);
	}
}
