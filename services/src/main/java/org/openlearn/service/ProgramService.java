package org.openlearn.service;

import org.openlearn.domain.Program;
import org.openlearn.domain.User;
import org.openlearn.repository.ProgramRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Program.
 */
@Service
@Transactional
public class ProgramService {

    private final Logger log = LoggerFactory.getLogger(ProgramService.class);

    private final ProgramRepository programRepository;

    private final UserRepository userRepository;

    public ProgramService(ProgramRepository programRepository, UserRepository userRepository) {
        this.programRepository = programRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a program.
     *
     * @param program the entity to save
     * @return the persisted entity
     */
    public Program save(Program program) {
        log.debug("Request to save Program : {}", program);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			return programRepository.save(program);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Program orig = findOne(program.getId());
		if(orig != null){
			return programRepository.save(program);
		}
		return null;
    }

    /**
     *  Get all the programs.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Program> findAll(Pageable pageable) {
        log.debug("Request to get all Programs");
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			List<Program> result = programRepository.findAll();
			return result;
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return programRepository.findAllByOrganization(user.get().getOrganizationIds());
    }

    /**
     *  Get one program by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Program findOne(Long id) {
        log.debug("Request to get Program : {}", id);
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
        	return programRepository.findOne(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        return programRepository.findOneByIdAndOrgIdsWithEagerRelationships(id, user.get().getOrganizationIds());
    }

    /**
     *  Delete the  program by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Program : {}", id);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			programRepository.delete(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Program orig = findOne(id);
		if(orig != null){
			programRepository.delete(id);
		}
    }
}
