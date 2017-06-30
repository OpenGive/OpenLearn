package org.openlearn.repository.search;

import org.openlearn.domain.ItemLink;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ItemLink entity.
 */
public interface ItemLinkSearchRepository extends ElasticsearchRepository<ItemLink, Long> {
}
