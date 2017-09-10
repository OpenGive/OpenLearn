package org.openlearn.service;

import org.openlearn.domain.Program;
import org.openlearn.domain.User;
import org.openlearn.dto.ProgramDTO;
import org.openlearn.repository.ProgramRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.ProgramTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Program.
 */
@Service
@Transactional
public class ProgramService {

	private final Logger log = LoggerFactory.getLogger(ProgramService.class);

	private final ProgramRepository programRepository;

	private final ProgramTransformer programTransformer;

	private final UserService userService;

	public ProgramService(ProgramRepository programRepository, ProgramTransformer programTransformer,
	                      UserService userService) {
		this.programRepository = programRepository;
		this.programTransformer = programTransformer;
		this.userService = userService;
	}

	/**
	 * Save a program.
	 *
	 * @param programDTO the entity to save
	 * @return the persisted entity
	 */
	public ProgramDTO save(ProgramDTO programDTO) {
		log.debug("Request to save Program : {}", programDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(programDTO)) {
			return programTransformer.transform(programRepository.save(programTransformer.transform(programDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the programs.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<ProgramDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Programs");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return programRepository.findAll(pageable).map(programTransformer::transform);
		} else {
			return programRepository.findAllByOrganization(user.getOrganization(), pageable)
				.map(programTransformer::transform);
		}
	}

	/**
	 * Get one program by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public ProgramDTO findOne(Long id) {
		log.debug("Request to get Program : {}", id);
		Program program = programRepository.findOne(id);
		if (program != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(program))) {
			return programTransformer.transform(program);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the program by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Program : {}", id);
		Program program = programRepository.findOne(id);
		if (program != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(program))) {
			programRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	/**
	 * Determines if a program is in the organization of current user
	 *
	 * @param programDTO the program
	 * @return true if program and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(ProgramDTO programDTO) {
		User user = userService.getCurrentUser();
		return user.getOrganization().getId().equals(programDTO.getOrganizationId());
	}

	/**
	 * Determines if a program is in the organization of current user
	 *
	 * @param program the program
	 * @return true if program and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(Program program) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(program.getOrganization());
	}
}
