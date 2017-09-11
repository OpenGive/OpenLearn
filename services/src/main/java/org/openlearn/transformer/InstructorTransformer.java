package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.InstructorDTO;
import org.openlearn.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InstructorTransformer {

	private static final Logger log = LoggerFactory.getLogger(InstructorTransformer.class);

	private final UserTransformer userTransformer;

	private final OrganizationRepository organizationRepository;

	public InstructorTransformer(final UserTransformer userTransformer,
	                             final OrganizationRepository organizationRepository) {
		this.userTransformer = userTransformer;
		this.organizationRepository = organizationRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param user entity to transform
	 * @return the new DTO
	 */
	public InstructorDTO transform(final User user) {
		log.debug("Transforming user to instructor DTO : {}", user);
		InstructorDTO instructorDTO = new InstructorDTO();
		userTransformer.transformUserToDTO(instructorDTO, user);
		instructorDTO.setOrganizationId(user.getOrganization().getId());
		instructorDTO.setOrgRole(user.getOrgRole());
		return instructorDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param instructorDTO DTO to transform
	 * @return the new entity
	 */
	public User transform(final InstructorDTO instructorDTO) {
		log.debug("Transforming instructor DTO to user : {}", instructorDTO);
		User user = new User();
		userTransformer.transformDTOToUser(user, instructorDTO);
		user.setOrganization(organizationRepository.findOne(instructorDTO.getOrganizationId()));
		user.setOrgRole(instructorDTO.getOrgRole());
		return user;
	}
}
