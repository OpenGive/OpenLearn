package org.openlearn.service;

import org.openlearn.domain.ItemLink;
import org.openlearn.repository.ItemLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing ItemLink.
 */
@Service
@Transactional
public class ItemLinkService {

	private final Logger log = LoggerFactory.getLogger(ItemLinkService.class);

	private final ItemLinkRepository itemLinkRepository;

	public ItemLinkService(final ItemLinkRepository itemLinkRepository) {
		this.itemLinkRepository = itemLinkRepository;
	}

	/**
	 * Save a itemLink.
	 *
	 * @param itemLink the entity to save
	 * @return the persisted entity
	 */
	public ItemLink save(final ItemLink itemLink) {
		log.debug("Request to save ItemLink : {}", itemLink);
		final ItemLink result = itemLinkRepository.save(itemLink);
		return result;
	}

	/**
	 *  Get all the itemLinks.
	 *
	 *  @param pageable the pagination information
	 *  @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<ItemLink> findAll(final Pageable pageable) {
		log.debug("Request to get all ItemLinks");
		final Page<ItemLink> result = itemLinkRepository.findAll(pageable);
		return result;
	}

	/**
	 *  Get one itemLink by id.
	 *
	 *  @param id the id of the entity
	 *  @return the entity
	 */
	@Transactional(readOnly = true)
	public ItemLink findOne(final Long id) {
		log.debug("Request to get ItemLink : {}", id);
		final ItemLink itemLink = itemLinkRepository.findOne(id);
		return itemLink;
	}

	/**
	 *  Delete the  itemLink by id.
	 *
	 *  @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete ItemLink : {}", id);
		itemLinkRepository.delete(id);
	}
}
