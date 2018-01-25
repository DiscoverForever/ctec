package com.cetc.cctv.repository.search;

import com.cetc.cctv.domain.Cluster;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cluster entity.
 */
public interface ClusterSearchRepository extends ElasticsearchRepository<Cluster, Long> {
}
