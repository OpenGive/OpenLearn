package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.User;
import org.openlearn.dto.InstructorDTO;
import org.openlearn.repository.UserRepository;
import org.openlearn.repository.AddressRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.InstructorTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing instructor users.
 */
@Service
@Transactional
public class InstructorService {

	private static final Authority INSTRUCTOR = new Authority(AuthoritiesConstants.INSTRUCTOR);

	private static final Logger log = LoggerFactory.getLogger(InstructorService.class);

	private final InstructorTransformer instructorTransformer;

	private final UserRepository userRepository;

	private final AddressRepository addressRepository;

	private final UserService userService;

	public InstructorService(final InstructorTransformer instructorTransformer, final UserRepository userRepository, final AddressRepository addressRepository,
	                         final UserService userService) {
		this.instructorTransformer = instructorTransformer;
		this.userRepository = userRepository;
		this.userService = userService;
		this.addressRepository = addressRepository;
	}

	/**
	 * Save an instructor user.
	 *
	 * @param instructorDTO the entity to save
	 * @return the persisted entity
	 */
	public InstructorDTO save(final InstructorDTO instructorDTO) {
		log.debug("Request to save instructor : {}", instructorDTO);
		if (AuthoritiesConstants.INSTRUCTOR.equals(instructorDTO.getAuthority())
			&& (SecurityUtils.isAdmin() || inOrgOfCurrentUser(instructorDTO))) {
			User user = userRepository.save(instructorTransformer.transform(instructorDTO));
			if (user.getAddress() != null) addressRepository.save(user.getAddress());
			return instructorTransformer.transform(user);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the instructor users.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<InstructorDTO> findAll(final Pageable pageable) {
		log.debug("Request to get all instructor users");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return userRepository.findByAuthority(INSTRUCTOR, pageable).map(instructorTransformer::transform);
		} else {
			return userRepository.findByOrganizationAndAuthority(user.getOrganization(), INSTRUCTOR, pageable)
				.map(instructorTransformer::transform);
		}
	}

	/**
	 * Get one user by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public InstructorDTO findOne(final Long id) {
		log.debug("Request to get instructor : {}", id);
		User instructor = userRepository.findOneByIdAndAuthority(id, INSTRUCTOR);
		if (instructor != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(instructor))) {
			return instructorTransformer.transform(instructor);
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
		log.debug("Request to delete instructor : {}", id);
		User instructor = userRepository.findOneByIdAndAuthority(id, INSTRUCTOR);
		if (instructor != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(instructor))) {
			userRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	private boolean inOrgOfCurrentUser(final InstructorDTO instructorDTO) {
		User user = userService.getCurrentUser();
		return user.getOrganization().getId().equals(instructorDTO.getOrganizationId());
	}

	private boolean inOrgOfCurrentUser(final User instructor) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(instructor.getOrganization());
	}
}
