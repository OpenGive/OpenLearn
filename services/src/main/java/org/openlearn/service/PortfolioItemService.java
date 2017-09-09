package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.repository.PortfolioItemRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.PortfolioItemTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing PortfolioItem.
 */
@Service
@Transactional
public class PortfolioItemService {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private final Logger log = LoggerFactory.getLogger(PortfolioItemService.class);

	private final PortfolioItemRepository portfolioItemRepository;

	private final PortfolioItemTransformer portfolioItemTransformer;

	private final UserRepository userRepository;

	private final UserService userService;

	public PortfolioItemService(PortfolioItemRepository portfolioItemRepository,
	                            PortfolioItemTransformer portfolioItemTransformer, UserRepository userRepository,
	                            UserService userService) {
		this.portfolioItemRepository = portfolioItemRepository;
		this.portfolioItemTransformer = portfolioItemTransformer;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	/**
	 * Save a portfolio item.
	 *
	 * @param portfolioItemDTO the entity to save
	 * @return the persisted entity
	 */
	public PortfolioItemDTO save(PortfolioItemDTO portfolioItemDTO) {
		log.debug("Request to save portfolio item : {}", portfolioItemDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(portfolioItemDTO)) {
			return portfolioItemTransformer.transform(portfolioItemRepository.save(portfolioItemTransformer.transform(portfolioItemDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the portfolio items.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<PortfolioItemDTO> findAll(Pageable pageable) {
		log.debug("Request to get all portfolio items");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return portfolioItemRepository.findAll(pageable).map(portfolioItemTransformer::transform);
		} else {
			return portfolioItemRepository.findAllByOrganization(user.getOrganization(), pageable)
				.map(portfolioItemTransformer::transform);
		}
	}

	/**
	 * Get one portfolio item by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public PortfolioItemDTO findOne(Long id) {
		log.debug("Request to get portfolio item : {}", id);
		PortfolioItem portfolioItem = portfolioItemRepository.findOne(id);
		if (portfolioItem != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(portfolioItem))) {
			return portfolioItemTransformer.transform(portfolioItem);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the portfolio item by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete portfolio item : {}", id);
		PortfolioItem portfolioItem = portfolioItemRepository.findOne(id);
		if (portfolioItem != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(portfolioItem))) {
			portfolioItemRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	/**
	 * Determines if a portfolio item is in the organization of current user
	 *
	 * @param portfolioItemDTO the portfolio item
	 * @return true if portfolio item and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(PortfolioItemDTO portfolioItemDTO) {
		User user = userService.getCurrentUser();
		User student = userRepository.findOneByIdAndAuthority(portfolioItemDTO.getStudentId(), STUDENT);
		return student != null && user.getOrganization().equals(student.getOrganization());
	}

	/**
	 * Determines if a portfolio item is in the organization of current user
	 *
	 * @param portfolioItem the portfolio item
	 * @return true if portfolio item and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(PortfolioItem portfolioItem) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(portfolioItem.getStudent().getOrganization());
	}
}
