package org.openlearn.repository.search;

import org.openlearn.domain.Session;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Session entity.
 */
public interface SessionSearchRepository extends ElasticsearchRepository<Session, Long> {
}
