package org.openlearn.repository.search;

import org.openlearn.domain.School;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the School entity.
 */
public interface SchoolSearchRepository extends ElasticsearchRepository<School, Long> {
}
