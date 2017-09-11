package org.openlearn.transformer;

import org.openlearn.domain.Organization;
import org.openlearn.dto.OrganizationDTO;
import org.openlearn.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrganizationTransformer {

	private static final Logger log = LoggerFactory.getLogger(OrganizationTransformer.class);

	private final OrganizationRepository organizationRepository;

	public OrganizationTransformer(final OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
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
		Organization organization = organizationDTO.getId() == null ? new Organization() : organizationRepository.findOne(organizationDTO.getId());
		// TODO: Error handling
		organization.setName(organizationDTO.getName());
		organization.setDescription(organizationDTO.getDescription());
		organization.setPrimaryContactName(organizationDTO.getPrimaryContactName());
		organization.setPrimaryContactInfo(organizationDTO.getPrimaryContactInfo());
		if (organizationDTO.getSecondaryContactName() != null) organization.setSecondaryContactName(organizationDTO.getSecondaryContactName());
		if (organizationDTO.getSecondaryContactInfo() != null) organization.setSecondaryContactInfo(organizationDTO.getSecondaryContactInfo());
		return organization;
	}
}
