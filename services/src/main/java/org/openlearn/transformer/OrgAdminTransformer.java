package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.OrgAdminDTO;
import org.openlearn.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrgAdminTransformer {

	private static final Logger log = LoggerFactory.getLogger(OrgAdminTransformer.class);

	private final UserTransformer userTransformer;

	private final OrganizationRepository organizationRepository;

	public OrgAdminTransformer(final UserTransformer userTransformer,
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
		User user = new User();
		userTransformer.transformDTOToUser(user, orgAdminDTO);
		user.setOrganization(organizationRepository.findOne(orgAdminDTO.getOrganizationId()));
		user.setOrgRole(orgAdminDTO.getOrgRole());
		return user;
	}
}
