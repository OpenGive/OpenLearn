package org.openlearn.service;

import org.openlearn.domain.Portfolio;
import org.openlearn.repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing Portfolio.
 */
@Service
@Transactional
public class PortfolioService {

    private final Logger log = LoggerFactory.getLogger(PortfolioService.class);

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
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
    }
}
