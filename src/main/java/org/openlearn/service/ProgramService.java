package org.openlearn.service;

import org.openlearn.domain.Program;
import org.openlearn.repository.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Program.
 */
@Service
@Transactional
public class ProgramService {

    private final Logger log = LoggerFactory.getLogger(ProgramService.class);

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    /**
     * Save a program.
     *
     * @param program the entity to save
     * @return the persisted entity
     */
    public Program save(Program program) {
        log.debug("Request to save Program : {}", program);
        Program result = programRepository.save(program);
        return result;
    }

    /**
     *  Get all the programs.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Program> findAll() {
        log.debug("Request to get all Programs");
        List<Program> result = programRepository.findAll();
        return result;
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
        Program program = programRepository.findOne(id);
        return program;
    }

    /**
     *  Delete the  program by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Program : {}", id);
        programRepository.delete(id);
    }
}
