package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.User;
import org.openlearn.dto.StudentDTO;
import org.openlearn.repository.AddressRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.StudentTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing student users.
 */
@Service
@Transactional
public class StudentService {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	private final StudentTransformer studentTransformer;

	private final UserRepository userRepository;

	private final AddressRepository addressRepository;

	private final UserService userService;

	public StudentService(final StudentTransformer studentTransformer, final UserRepository userRepository, final AddressRepository addressRepository,
	                      final UserService userService) {
		this.studentTransformer = studentTransformer;
		this.userRepository = userRepository;
		this.userService = userService;
		this.addressRepository = addressRepository;
	}

	/**
	 * Save a student user.
	 *
	 * @param studentDTO the entity to save
	 * @return the persisted entity
	 */
	public StudentDTO save(final StudentDTO studentDTO) {
		log.debug("Request to save student : {}", studentDTO);
		if (AuthoritiesConstants.STUDENT.equals(studentDTO.getAuthority())
			&& (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentDTO))) {
			User user = userRepository.save(studentTransformer.transform(studentDTO));
			if (user.getAddress() != null) addressRepository.save(user.getAddress());
			return studentTransformer.transform(user);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the student users.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<StudentDTO> findAll() {
		log.debug("Request to get all student users");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return userRepository.findByAuthority(STUDENT)
				.stream()
				.map(studentTransformer::transform)
				.collect(Collectors.toList());
		} else {
			return userRepository.findByOrganizationAndAuthority(user.getOrganization(), STUDENT)
				.stream()
				.map(studentTransformer::transform)
				.collect(Collectors.toList());
		}
	}

	@Transactional(readOnly = true)
	public List<StudentDTO> findStudentsNotInCourse(final Long courseId) {
		log.debug("Request to get all StudentsNotInCourseByOrganization");
		if (SecurityUtils.isAdmin()) {
			return userRepository.findStudentsNotInCourse(courseId)
				.stream()
				.map(studentTransformer::transform)
				.collect(Collectors.toList());
		} else {
			User user = userService.getCurrentUser();
			return userRepository.findStudentsNotInCourseByOrganization(courseId,user.getOrganization().getId())
				.stream()
				.map(studentTransformer::transform)
				.collect(Collectors.toList());
		}
	}

	/**
	 * Get one user by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public StudentDTO findOne(final Long id) {
		log.debug("Request to get student : {}", id);
		User student = userRepository.findOneByIdAndAuthority(id, STUDENT);
		if (student != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(student))) {
			return studentTransformer.transform(student);
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
		log.debug("Request to delete student : {}", id);
		User student = userRepository.findOneByIdAndAuthority(id, STUDENT);
		if (student != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(student))) {
			// TODO: Use Address service
			if (student.getAddress() != null) addressRepository.delete(student.getAddress().getId());
			userRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	private boolean inOrgOfCurrentUser(final StudentDTO studentDTO) {
		User user = userService.getCurrentUser();
		return user.getOrganization().getId().equals(studentDTO.getOrganizationId());
	}

	private boolean inOrgOfCurrentUser(final User student) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(student.getOrganization());
	}
}
