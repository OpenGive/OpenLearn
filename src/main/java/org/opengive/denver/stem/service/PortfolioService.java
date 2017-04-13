package org.opengive.denver.stem.service;

import org.opengive.denver.stem.domain.Portfolio;
import org.opengive.denver.stem.repository.PortfolioRepository;
import org.opengive.denver.stem.repository.search.PortfolioSearchRepository;
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
 * Service Implementation for managing Portfolio.
 */
@Service
@Transactional
public class PortfolioService {

    private final Logger log = LoggerFactory.getLogger(PortfolioService.class);
    
    private final PortfolioRepository portfolioRepository;

    private final PortfolioSearchRepository portfolioSearchRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, PortfolioSearchRepository portfolioSearchRepository) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioSearchRepository = portfolioSearchRepository;
    }

    /**
     * Save a portfolio.
     *
     * @param portfolio the entity to save
     * @return the persisted entity
     */
    public Portfolio save(Portfolio portfolio) {
        log.debug("Request to save Portfolio : {}", portfolio);
        Portfolio result = portfolioRepository.save(portfolio);
        portfolioSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the portfolios.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Portfolio> findAll(Pageable pageable) {
        log.debug("Request to get all Portfolios");
        Page<Portfolio> result = portfolioRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one portfolio by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Portfolio findOne(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        Portfolio portfolio = portfolioRepository.findOne(id);
        return portfolio;
    }

    /**
     *  Delete the  portfolio by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Portfolio : {}", id);
        portfolioRepository.delete(id);
        portfolioSearchRepository.delete(id);
    }

    /**
     * Search for the portfolio corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Portfolio> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Portfolios for query {}", query);
        Page<Portfolio> result = portfolioSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
