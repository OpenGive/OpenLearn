package org.openlearn.repository.search;

import org.openlearn.domain.Achievement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Achievement entity.
 */
public interface AchievementSearchRepository extends ElasticsearchRepository<Achievement, Long> {
}
