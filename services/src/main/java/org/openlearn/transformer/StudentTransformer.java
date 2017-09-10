package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.domain.enumeration.GradeLevel;
import org.openlearn.dto.StudentDTO;
import org.openlearn.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentTransformer {

	private final UserTransformer userTransformer;

	private final OrganizationRepository organizationRepository;

	public StudentTransformer(UserTransformer userTransformer, OrganizationRepository organizationRepository) {
		this.userTransformer = userTransformer;
		this.organizationRepository = organizationRepository;
	}

	public StudentDTO transform(final User user) {
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

	public User transform(final StudentDTO studentDTO) {
		User user = new User();
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
