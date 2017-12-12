package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.FileInformation;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.repository.PortfolioItemRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.PortfolioItemTransformer;
import org.openlearn.web.rest.errors.AccessDeniedException;
import org.openlearn.web.rest.errors.PortfolioItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.ShellProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PortfolioItem.
 */
@Service
@Transactional
public class PortfolioItemService {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private static final Logger log = LoggerFactory.getLogger(PortfolioItemService.class);

	private final PortfolioItemRepository portfolioItemRepository;

	private final PortfolioItemTransformer portfolioItemTransformer;

	private final StudentAssignmentService studentAssignmentService;

	private final StudentCourseService studentCourseService;

	private final UserRepository userRepository;

	private final UserService userService;

	private final FileInformationService fileInformationService;

	public PortfolioItemService(final PortfolioItemRepository portfolioItemRepository,
								final PortfolioItemTransformer portfolioItemTransformer,
								final StudentAssignmentService studentAssignmentService,
								final StudentCourseService studentCourseService, final UserRepository userRepository,
								final UserService userService,
								final FileInformationService fileInformationService) {
		this.portfolioItemRepository = portfolioItemRepository;
		this.portfolioItemTransformer = portfolioItemTransformer;
		this.studentAssignmentService = studentAssignmentService;
		this.studentCourseService = studentCourseService;
		this.userRepository = userRepository;
		this.userService = userService;
		this.fileInformationService = fileInformationService;
	}

	/**
	 * Save a portfolio item.
	 *
	 * @param portfolioItemDTO the entity to save
	 * @return the persisted entity
	 */
	public PortfolioItemDTO save(final PortfolioItemDTO portfolioItemDTO) {
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
	public Page<PortfolioItemDTO> findAll(final Pageable pageable) {
		log.debug("Request to get all portfolio items");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return portfolioItemRepository.findAll(pageable).map(portfolioItemTransformer::transform);
		} else {
			return portfolioItemRepository.findByOrganization(user.getOrganization(), pageable)
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
	public PortfolioItemDTO findOne(final Long id) {
		log.debug("Request to get portfolio item : {}", id);
		PortfolioItem portfolioItem = portfolioItemRepository.findOne(id);
		if (portfolioItem != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(portfolioItem))) {
			return portfolioItemTransformer.transform(portfolioItem);
		}
		// TODO: Error handling / logging
		return null;
	}

	public List<PortfolioItemDTO> getPortfolioForStudent(final Long id) {
		log.debug("Request to get portfolio for student : {}", id);
		User student = userRepository.findOneByIdAndAuthority(id, STUDENT);
		if (student != null) {
			List<PortfolioItemDTO> portfolio = portfolioItemRepository.findByStudent(student).stream()
				.map(portfolioItemTransformer::transform).collect(Collectors.toList());
			portfolio.addAll(studentAssignmentService.findFlaggedByStudent(id).stream()
				.map(portfolioItemTransformer::transform).collect(Collectors.toList()));
			portfolio.addAll(studentCourseService.findFlaggedByStudent(id).stream()
				.map(portfolioItemTransformer::transform).collect(Collectors.toList()));
			return portfolio;
		}
		// TODO: Error handling
		return null;
	}

	/**
	 * Delete the portfolio item by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete portfolio item : {}", id);
		PortfolioItem portfolioItem = portfolioItemRepository.findOne(id);
		if (portfolioItem == null) new PortfolioItemNotFoundException(id);

		if ((SecurityUtils.isAdmin() || inOrgOfCurrentUser(portfolioItem))) {
			fileInformationService.deleteByPortfolioItem(portfolioItem);
			portfolioItemRepository.delete(id);
		} else {
			throw new AccessDeniedException();
		}
	}

	public boolean inOrgOfCurrentUser(final PortfolioItemDTO portfolioItemDTO) {
		User user = userService.getCurrentUser();
		User student = userRepository.findOneByIdAndAuthority(portfolioItemDTO.getStudentId(), STUDENT);
		return student != null && SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORG_ADMIN) &&
            user.getOrganization().equals(student.getOrganization());
	}

	public boolean inOrgOfCurrentUser(final PortfolioItem portfolioItem) {
		User user = userService.getCurrentUser();
		return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORG_ADMIN) &&
		user.getOrganization().equals(portfolioItem.getStudent().getOrganization());
	}
}
