package com.cetc.cctv.repository.search;

import com.cetc.cctv.domain.AlarmRegion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AlarmRegion entity.
 */
public interface AlarmRegionSearchRepository extends ElasticsearchRepository<AlarmRegion, Long> {
}
