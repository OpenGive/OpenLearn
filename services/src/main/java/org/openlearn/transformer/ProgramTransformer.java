package org.openlearn.transformer;

import org.openlearn.domain.Program;
import org.openlearn.dto.ProgramDTO;
import org.openlearn.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class ProgramTransformer {

	private final OrganizationRepository organizationRepository;

	public ProgramTransformer(OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}

	public ProgramDTO transform(final Program program) {
		ProgramDTO programDTO = new ProgramDTO();
		programDTO.setId(program.getId());
		programDTO.setName(program.getName());
		programDTO.setDescription(program.getDescription());
		programDTO.setOrganizationId(program.getOrganization().getId());
		return programDTO;
	}

	public Program transform(final ProgramDTO programDTO) {
		Program program = new Program();
		program.setId(programDTO.getId());
		program.setName(programDTO.getName());
		program.setDescription(programDTO.getDescription());
		program.setOrganization(organizationRepository.findOne(programDTO.getOrganizationId()));
		return program;
	}
}
