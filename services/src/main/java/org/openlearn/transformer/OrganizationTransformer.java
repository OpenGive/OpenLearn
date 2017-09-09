package org.openlearn.transformer;

import org.openlearn.domain.Organization;
import org.openlearn.dto.OrganizationDTO;
import org.springframework.stereotype.Service;

@Service
public class OrganizationTransformer {

	public OrganizationTransformer() {
	}

	public OrganizationDTO transform(final Organization organization) {
		OrganizationDTO organizationDTO = new OrganizationDTO();
		organizationDTO.setId(organization.getId());
		organizationDTO.setName(organization.getName());
		organizationDTO.setDescription(organization.getDescription());
		organizationDTO.setPrimaryContactName(organization.getPrimaryContactName());
		organizationDTO.setPrimaryContactInfo(organization.getPrimaryContactInfo());
		organizationDTO.setSecondaryContactName(organization.getSecondaryContactName());
		organizationDTO.setSecondaryContactInfo(organization.getSecondaryContactInfo());
		return organizationDTO;
	}

	public Organization transform(final OrganizationDTO organizationDTO) {
		Organization organization = new Organization();
		organization.setId(organizationDTO.getId());
		organization.setName(organizationDTO.getName());
		organization.setDescription(organizationDTO.getDescription());
		organization.setPrimaryContactName(organizationDTO.getPrimaryContactName());
		organization.setPrimaryContactInfo(organizationDTO.getPrimaryContactInfo());
		organization.setSecondaryContactName(organizationDTO.getSecondaryContactName());
		organization.setSecondaryContactInfo(organizationDTO.getSecondaryContactInfo());
		return organization;
	}
}
