package org.openlearn.service;

import org.openlearn.domain.School;
import org.openlearn.repository.SchoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing School.
 */
@Service
@Transactional
public class SchoolService {

    private final Logger log = LoggerFactory.getLogger(SchoolService.class);

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    /**
     * Save a school.
     *
     * @param school the entity to save
     * @return the persisted entity
     */
    public School save(School school) {
        log.debug("Request to save School : {}", school);
        School result = schoolRepository.save(school);
        return result;
    }

    /**
     *  Get all the schools.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<School> findAll() {
        log.debug("Request to get all Schools");
        List<School> result = schoolRepository.findAll();
        return result;
    }

    /**
     *  Get one school by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public School findOne(Long id) {
        log.debug("Request to get School : {}", id);
        School school = schoolRepository.findOne(id);
        return school;
    }

    /**
     *  Delete the  school by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete School : {}", id);
        schoolRepository.delete(id);
    }
}
