package org.opengive.denver.stem.repository.search;

import org.opengive.denver.stem.domain.ItemLink;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ItemLink entity.
 */
public interface ItemLinkSearchRepository extends ElasticsearchRepository<ItemLink, Long> {
}
