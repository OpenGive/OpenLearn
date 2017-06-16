package org.opengive.denver.stem.repository.search;

import org.opengive.denver.stem.domain.Milestone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Milestone entity.
 */
public interface MilestoneSearchRepository extends ElasticsearchRepository<Milestone, Long> {
}
