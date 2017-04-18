package org.opengive.denver.stem.repository.search;

import org.opengive.denver.stem.domain.Portfolio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Portfolio entity.
 */
public interface PortfolioSearchRepository extends ElasticsearchRepository<Portfolio, Long> {
}
