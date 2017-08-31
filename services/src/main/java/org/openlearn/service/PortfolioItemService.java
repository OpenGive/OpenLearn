package org.openlearn.service;

import org.openlearn.domain.PortfolioItem;
import org.openlearn.repository.PortfolioItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing PortfolioItem.
 */
@Service
@Transactional
public class PortfolioItemService {

	private final Logger log = LoggerFactory.getLogger(PortfolioItemService.class);

	private final PortfolioItemRepository portfolioItemRepository;

	public PortfolioItemService(PortfolioItemRepository portfolioItemRepository) {
		this.portfolioItemRepository = portfolioItemRepository;
	}

	/**
	 * Save a portfolioItem.
	 *
	 * @param portfolioItem the entity to save
	 * @return the persisted entity
	 */
	public PortfolioItem save(PortfolioItem portfolioItem) {
		log.debug("Request to save PortfolioItem : {}", portfolioItem);
		return portfolioItemRepository.save(portfolioItem);
	}

	/**
	 * Get all the portfolioItems.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<PortfolioItem> findAll(Pageable pageable) {
		log.debug("Request to get all PortfolioItems");
		return portfolioItemRepository.findAll(pageable);
	}

	/**
	 * Get one portfolioItem by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public PortfolioItem findOne(Long id) {
		log.debug("Request to get PortfolioItem : {}", id);
		return portfolioItemRepository.findOne(id);
	}

	/**
	 * Delete the  portfolioItem by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete PortfolioItem : {}", id);
		portfolioItemRepository.delete(id);
	}
}
