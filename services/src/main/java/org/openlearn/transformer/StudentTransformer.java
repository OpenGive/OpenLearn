package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.domain.enumeration.GradeLevel;
import org.openlearn.dto.StudentDTO;
import org.openlearn.repository.OrganizationRepository;
import org.openlearn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentTransformer {

	private static final Logger log = LoggerFactory.getLogger(StudentTransformer.class);

	private final OrganizationRepository organizationRepository;

	private final UserRepository userRepository;

	private final UserTransformer userTransformer;

	public StudentTransformer(final OrganizationRepository organizationRepository, final UserRepository userRepository,
	                          final UserTransformer userTransformer) {
		this.organizationRepository = organizationRepository;
		this.userRepository = userRepository;
		this.userTransformer = userTransformer;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param user entity to transform
	 * @return the new DTO
	 */
	public StudentDTO transform(final User user) {
		log.debug("Transforming user to student DTO : {}", user);
		StudentDTO studentDTO = new StudentDTO();
		userTransformer.transformUserToDTO(studentDTO, user);
		studentDTO.setOrganizationId(user.getOrganization().getId());
		studentDTO.setFourteenPlus(user.getFourteenPlus());
		studentDTO.setGuardianFirstName(user.getGuardianFirstName());
		studentDTO.setGuardianLastName(user.getGuardianLastName());
		studentDTO.setGuardianEmail(user.getGuardianEmail());
		studentDTO.setGuardianPhone(user.getGuardianPhone());
		studentDTO.setSchool(user.getSchool());
		studentDTO.setGradeLevel(user.getGradeLevel().name());
		studentDTO.setStateStudentId(user.getStateStudentId());
		studentDTO.setOrgStudentId(user.getStateStudentId());
		return studentDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param studentDTO DTO to transform
	 * @return the new entity
	 */
	public User transform(final StudentDTO studentDTO) {
		log.debug("Transforming student DTO to user : {}", studentDTO);
		User user = studentDTO.getId() == null ? new User() : userRepository.findOne(studentDTO.getId());
		// TODO: Error handling
		userTransformer.transformDTOToUser(user, studentDTO);
		user.setOrganization(organizationRepository.findOne(studentDTO.getOrganizationId()));
		user.setFourteenPlus(studentDTO.getFourteenPlus());
		user.setGuardianFirstName(studentDTO.getGuardianFirstName());
		user.setGuardianLastName(studentDTO.getGuardianLastName());
		user.setGuardianEmail(studentDTO.getGuardianEmail());
		user.setGuardianPhone(studentDTO.getGuardianPhone());
		user.setSchool(studentDTO.getSchool());
		user.setGradeLevel(GradeLevel.valueOf(studentDTO.getGradeLevel()));
		user.setStateStudentId(studentDTO.getStateStudentId());
		user.setOrgStudentId(studentDTO.getOrgStudentId());
		return user;
	}
}
