package org.openlearn.repository.search;

import org.openlearn.domain.Milestone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Milestone entity.
 */
public interface MilestoneSearchRepository extends ElasticsearchRepository<Milestone, Long> {
}
