package org.openlearn.repository.search;

import org.openlearn.domain.PortfolioItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PortfolioItem entity.
 */
public interface PortfolioItemSearchRepository extends ElasticsearchRepository<PortfolioItem, Long> {
}
