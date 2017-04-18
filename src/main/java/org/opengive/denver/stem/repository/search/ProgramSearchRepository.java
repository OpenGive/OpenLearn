package org.opengive.denver.stem.repository.search;

import org.opengive.denver.stem.domain.Program;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Program entity.
 */
public interface ProgramSearchRepository extends ElasticsearchRepository<Program, Long> {
}
