package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.OrgAdminDTO;
import org.openlearn.repository.OrganizationRepository;
import org.openlearn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrgAdminTransformer {

	private static final Logger log = LoggerFactory.getLogger(OrgAdminTransformer.class);

	private final OrganizationRepository organizationRepository;

	private final UserRepository userRepository;

	private final UserTransformer userTransformer;

	public OrgAdminTransformer(final OrganizationRepository organizationRepository,
	                           final UserRepository userRepository, final UserTransformer userTransformer) {
		this.userTransformer = userTransformer;
		this.userRepository = userRepository;
		this.organizationRepository = organizationRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param user entity to transform
	 * @return the new DTO
	 */
	public OrgAdminDTO transform(final User user) {
		log.debug("Transforming user to org admin DTO : {}", user);
		OrgAdminDTO orgAdminDTO = new OrgAdminDTO();
		userTransformer.transformUserToDTO(orgAdminDTO, user);
		orgAdminDTO.setOrganizationId(user.getOrganization().getId());
		orgAdminDTO.setOrgRole(user.getOrgRole());
		return orgAdminDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param orgAdminDTO DTO to transform
	 * @return the new entity
	 */
	public User transform(final OrgAdminDTO orgAdminDTO) {
		log.debug("Transforming org admin DTO to user : {}", orgAdminDTO);
		User user = orgAdminDTO.getId() == null ? new User() : userRepository.findOne(orgAdminDTO.getId());
		// TODO: Error handling
		userTransformer.transformDTOToUser(user, orgAdminDTO);
		user.setOrganization(organizationRepository.findOne(orgAdminDTO.getOrganizationId()));
		user.setOrgRole(orgAdminDTO.getOrgRole());
		return user;
	}
}
