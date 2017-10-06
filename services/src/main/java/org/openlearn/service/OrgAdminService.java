package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.User;
import org.openlearn.dto.OrgAdminDTO;
import org.openlearn.repository.UserRepository;
import org.openlearn.repository.AddressRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.OrgAdminTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing org admin users.
 */
@Service
@Transactional
public class OrgAdminService {

	private static final Authority ORG_ADMIN = new Authority(AuthoritiesConstants.ORG_ADMIN);

	private static final Logger log = LoggerFactory.getLogger(OrgAdminService.class);

	private final OrgAdminTransformer orgAdminTransformer;

	private final UserRepository userRepository;

	private final AddressRepository addressRepository;

	private final UserService userService;

	public OrgAdminService(final OrgAdminTransformer orgAdminTransformer, final UserRepository userRepository, final AddressRepository addressRepository,
	                       final UserService userService) {
		this.orgAdminTransformer = orgAdminTransformer;
		this.userRepository = userRepository;
		this.userService = userService;
		this.addressRepository = addressRepository;
	}

	/**
	 * Save an org admin user.
	 *
	 * @param orgAdminDTO the entity to save
	 * @return the persisted entity
	 */
	public OrgAdminDTO save(final OrgAdminDTO orgAdminDTO) {
		log.debug("Request to save org admin : {}", orgAdminDTO);
		if (AuthoritiesConstants.ORG_ADMIN.equals(orgAdminDTO.getAuthority())
			&& (SecurityUtils.isAdmin() || inOrgOfCurrentUser(orgAdminDTO))) {
			User user = userRepository.save(orgAdminTransformer.transform(orgAdminDTO));
			if (user.getAddress() != null) addressRepository.save(user.getAddress());
			return orgAdminTransformer.transform(user);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the org admin users.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<OrgAdminDTO> findAll(final Pageable pageable) {
		log.debug("Request to get all org admin users");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return userRepository.findByAuthority(ORG_ADMIN, pageable).map(orgAdminTransformer::transform);
		} else {
			return userRepository.findByOrganizationAndAuthority(user.getOrganization(), ORG_ADMIN, pageable)
				.map(orgAdminTransformer::transform);
		}
	}

	/**
	 * Get one user by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public OrgAdminDTO findOne(final Long id) {
		log.debug("Request to get org admin : {}", id);
		User orgAdmin = userRepository.findOneByIdAndAuthority(id, ORG_ADMIN);
		if (orgAdmin != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(orgAdmin))) {
			return orgAdminTransformer.transform(orgAdmin);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the user by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete org admin : {}", id);
		User orgAdmin = userRepository.findOneByIdAndAuthority(id, ORG_ADMIN);
		if (orgAdmin != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(orgAdmin))) {
			userRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	private boolean inOrgOfCurrentUser(final OrgAdminDTO orgAdminDTO) {
		User user = userService.getCurrentUser();
		return user.getOrganization().getId().equals(orgAdminDTO.getOrganizationId());
	}

	private boolean inOrgOfCurrentUser(final User orgAdmin) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(orgAdmin.getOrganization());
	}
}
