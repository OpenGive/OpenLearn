package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.InstructorDTO;
import org.openlearn.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class InstructorTransformer {

	private final UserTransformer userTransformer;

	private final OrganizationRepository organizationRepository;

	public InstructorTransformer(UserTransformer userTransformer, OrganizationRepository organizationRepository) {
		this.userTransformer = userTransformer;
		this.organizationRepository = organizationRepository;
	}

	public InstructorDTO transform(final User user) {
		InstructorDTO instructorDTO = new InstructorDTO();
		userTransformer.transformUserToDTO(instructorDTO, user);
		instructorDTO.setOrganizationId(user.getOrganization().getId());
		instructorDTO.setOrgRole(user.getOrgRole());
		return instructorDTO;
	}

	public User transform(final InstructorDTO instructorDTO) {
		User user = new User();
		userTransformer.transformDTOToUser(user, instructorDTO);
		user.setOrganization(organizationRepository.findOne(instructorDTO.getOrganizationId()));
		user.setOrgRole(instructorDTO.getOrgRole());
		return user;
	}
}
