package org.opengive.denver.stem.repository.search;

import org.opengive.denver.stem.domain.Achievement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Achievement entity.
 */
public interface AchievementSearchRepository extends ElasticsearchRepository<Achievement, Long> {
}
