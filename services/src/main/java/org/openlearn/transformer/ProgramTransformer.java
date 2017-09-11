package org.openlearn.transformer;

import org.openlearn.domain.Program;
import org.openlearn.dto.ProgramDTO;
import org.openlearn.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProgramTransformer {

	private static final Logger log = LoggerFactory.getLogger(ProgramTransformer.class);

	private final OrganizationRepository organizationRepository;

	public ProgramTransformer(final OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param program entity to transform
	 * @return the new DTO
	 */
	public ProgramDTO transform(final Program program) {
		log.debug("Transforming program to program DTO : {}", program);
		ProgramDTO programDTO = new ProgramDTO();
		programDTO.setId(program.getId());
		programDTO.setName(program.getName());
		programDTO.setDescription(program.getDescription());
		programDTO.setOrganizationId(program.getOrganization().getId());
		return programDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param programDTO DTO to transform
	 * @return the new entity
	 */
	public Program transform(final ProgramDTO programDTO) {
		log.debug("Transforming program DTO to program : {}", programDTO);
		Program program = new Program();
		program.setId(programDTO.getId());
		program.setName(programDTO.getName());
		program.setDescription(programDTO.getDescription());
		program.setOrganization(organizationRepository.findOne(programDTO.getOrganizationId()));
		return program;
	}
}
