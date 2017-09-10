package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.User;
import org.openlearn.dto.AdminDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.transformer.AdminTransformer;
import org.openlearn.repository.UserRepository;
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

	private final Logger log = LoggerFactory.getLogger(AdminService.class);

	private final AdminTransformer adminTransformer;

	private final UserRepository userRepository;

	public AdminService(AdminTransformer adminTransformer, UserRepository userRepository) {
		this.adminTransformer = adminTransformer;
		this.userRepository = userRepository;
	}

	/**
	 * Save an admin user.
	 *
	 * @param adminDTO the entity to save
	 * @return the persisted entity
	 */
	public AdminDTO save(AdminDTO adminDTO) {
		log.debug("Request to save admin : {}", adminDTO);
		return adminTransformer.transform(userRepository.save(adminTransformer.transform(adminDTO)));
	}

	/**
	 * Get all the admin users.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<AdminDTO> findAll(Pageable pageable) {
		log.debug("Request to get all admin users");
		return userRepository.findAllByAuthority(ADMIN, pageable).map(adminTransformer::transform);
	}

	/**
	 * Get one user by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public AdminDTO findOne(Long id) {
		log.debug("Request to get admin : {}", id);
		return adminTransformer.transform(userRepository.findOneByIdAndAuthority(id, ADMIN));
	}

	/**
	 * Delete the  user by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete admin : {}", id);
		User admin = userRepository.findOneByIdAndAuthority(id, ADMIN);
		if (admin != null) {
			userRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}
}
