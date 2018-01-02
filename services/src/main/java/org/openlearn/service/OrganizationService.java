package org.openlearn.service;

import org.openlearn.domain.Organization;
import org.openlearn.domain.User;
import org.openlearn.dto.OrganizationDTO;
import org.openlearn.repository.OrganizationRepository;
import org.openlearn.repository.ProgramRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.OrganizationTransformer;
import org.openlearn.web.rest.errors.ItemHasChildrenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Organization.
 */
@Service
@Transactional
public class OrganizationService {

	private static final Logger log = LoggerFactory.getLogger(OrganizationService.class);

	private final OrganizationRepository organizationRepository;

	private final OrganizationTransformer organizationTransformer;

	private final UserService userService;

	private final ProgramRepository programRepository;

	private final UserRepository userRepository;

	public OrganizationService(final OrganizationRepository organizationRepository,
							   final OrganizationTransformer organizationTransformer,
							   final UserService userService,
							   final ProgramRepository programRepository,
							   final UserRepository userRepository) {
		this.organizationRepository = organizationRepository;
		this.organizationTransformer = organizationTransformer;
		this.userService = userService;
		this.programRepository = programRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Save an organization.
	 *
	 * @param organizationDTO the entity to save
	 * @return the persisted entity
	 */
	public OrganizationDTO save(final OrganizationDTO organizationDTO) {
		log.debug("Request to save Organization : {}", organizationDTO);
		if (SecurityUtils.isAdmin() || isOrgOfCurrentUser(organizationDTO)) {
			return organizationTransformer.transform(organizationRepository.save(organizationTransformer.transform(organizationDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the organizations.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<OrganizationDTO> findAll() {
		log.debug("Request to get all Organizations");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return organizationRepository.findAll()
				.stream()
				.map(organizationTransformer::transform)
				.collect(Collectors.toList());
		} else {
			return organizationRepository.findById(user.getOrganization().getId())
				.stream()
				.map(organizationTransformer::transform)
				.collect(Collectors.toList());
		}
	}

	/**
	 * Get one organization by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public OrganizationDTO findOne(final Long id) {
		log.debug("Request to get Organization : {}", id);
		Organization organization = organizationRepository.findOne(id);
		if (organization != null && (SecurityUtils.isAdmin() || isOrgOfCurrentUser(organization))) {
			return organizationTransformer.transform(organization);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the organization by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Organization : {}", id);

		Organization organization = organizationRepository.findOneById(id);

		if (programRepository.existsByOrganization(organization) || userRepository.existsByOrganization(organization)) {
			throw new ItemHasChildrenException("Before you delete this organization, please remove all users and programs from the organization.");
		}

		organizationRepository.delete(id);
	}

	private boolean isOrgOfCurrentUser(final OrganizationDTO organizationDTO) {
		User user = userService.getCurrentUser();
		return user.getOrganization().getId().equals(organizationDTO.getId());
	}

	private boolean isOrgOfCurrentUser(final Organization organization) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(organization);
	}
}
