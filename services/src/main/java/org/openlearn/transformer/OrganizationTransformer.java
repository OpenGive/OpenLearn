package org.openlearn.transformer;

import org.openlearn.domain.Organization;
import org.openlearn.dto.OrganizationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrganizationTransformer {

	private static final Logger log = LoggerFactory.getLogger(OrganizationTransformer.class);

	public OrganizationTransformer() {
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param organization entity to transform
	 * @return the new DTO
	 */
	public OrganizationDTO transform(final Organization organization) {
		log.debug("Transforming organization to organization DTO : {}", organization);
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

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param organizationDTO DTO to transform
	 * @return the new entity
	 */
	public Organization transform(final OrganizationDTO organizationDTO) {
		log.debug("Transforming organization DTO to organization : {}", organizationDTO);
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
