package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.User;
import org.openlearn.dto.AdminDTO;
import org.openlearn.repository.UserRepository;
import org.openlearn.repository.AddressRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.transformer.AdminTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing admin users.
 */
@Service
@Transactional
public class AdminService {

	private static final Authority ADMIN = new Authority(AuthoritiesConstants.ADMIN);

	private static final Logger log = LoggerFactory.getLogger(AdminService.class);

	private final AdminTransformer adminTransformer;

	private final UserRepository userRepository;

	private final AddressRepository addressRepository;

	public AdminService(final AdminTransformer adminTransformer, final UserRepository userRepository, final AddressRepository addressRepository) {
		this.adminTransformer = adminTransformer;
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
	}

	/**
	 * Save an admin user.
	 *
	 * @param adminDTO the entity to save
	 * @return the persisted entity
	 */
	public AdminDTO save(final AdminDTO adminDTO) {
		log.debug("Request to save admin : {}", adminDTO);
		User user = userRepository.save(adminTransformer.transform(adminDTO));
		if (user.getAddress() != null) addressRepository.save(user.getAddress());
		return adminTransformer.transform(user);
	}

	/**
	 * Get all the admin users.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<AdminDTO> findAll(final Pageable pageable) {
		log.debug("Request to get all admin users");
		return userRepository.findByAuthority(ADMIN, pageable).map(adminTransformer::transform);
	}

	/**
	 * Get one user by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public AdminDTO findOne(final Long id) {
		log.debug("Request to get admin : {}", id);
		return adminTransformer.transform(userRepository.findOneByIdAndAuthority(id, ADMIN));
	}

	/**
	 * Delete the  user by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete admin : {}", id);
		User admin = userRepository.findOneByIdAndAuthority(id, ADMIN);
		if (admin != null) {
			userRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}
}
