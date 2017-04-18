package org.opengive.denver.stem.service;

import org.opengive.denver.stem.domain.Milestone;
import org.opengive.denver.stem.repository.MilestoneRepository;
import org.opengive.denver.stem.repository.search.MilestoneSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Milestone.
 */
@Service
@Transactional
public class MilestoneService {

    private final Logger log = LoggerFactory.getLogger(MilestoneService.class);
    
    private final MilestoneRepository milestoneRepository;

    private final MilestoneSearchRepository milestoneSearchRepository;

    public MilestoneService(MilestoneRepository milestoneRepository, MilestoneSearchRepository milestoneSearchRepository) {
        this.milestoneRepository = milestoneRepository;
        this.milestoneSearchRepository = milestoneSearchRepository;
    }

    /**
     * Save a milestone.
     *
     * @param milestone the entity to save
     * @return the persisted entity
     */
    public Milestone save(Milestone milestone) {
        log.debug("Request to save Milestone : {}", milestone);
        Milestone result = milestoneRepository.save(milestone);
        milestoneSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the milestones.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Milestone> findAll(Pageable pageable) {
        log.debug("Request to get all Milestones");
        Page<Milestone> result = milestoneRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one milestone by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Milestone findOne(Long id) {
        log.debug("Request to get Milestone : {}", id);
        Milestone milestone = milestoneRepository.findOne(id);
        return milestone;
    }

    /**
     *  Delete the  milestone by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Milestone : {}", id);
        milestoneRepository.delete(id);
        milestoneSearchRepository.delete(id);
    }

    /**
     * Search for the milestone corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Milestone> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Milestones for query {}", query);
        Page<Milestone> result = milestoneSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
