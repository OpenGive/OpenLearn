package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.OrgAdminDTO;
import org.openlearn.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrgAdminTransformer {

	private final UserTransformer userTransformer;

	private final OrganizationRepository organizationRepository;

	public OrgAdminTransformer(UserTransformer userTransformer, OrganizationRepository organizationRepository) {
		this.userTransformer = userTransformer;
		this.organizationRepository = organizationRepository;
	}

	public OrgAdminDTO transform(final User user) {
		OrgAdminDTO orgAdminDTO = new OrgAdminDTO();
		userTransformer.transformUserToDTO(orgAdminDTO, user);
		orgAdminDTO.setOrganizationId(user.getOrganization().getId());
		orgAdminDTO.setOrgRole(user.getOrgRole());
		return orgAdminDTO;
	}

	public User transform(final OrgAdminDTO orgAdminDTO) {
		User user = new User();
		userTransformer.transformDTOToUser(user, orgAdminDTO);
		user.setOrganization(organizationRepository.findOne(orgAdminDTO.getOrganizationId()));
		user.setOrgRole(orgAdminDTO.getOrgRole());
		return user;
	}
}
