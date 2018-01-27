package com.cetc.cctv.repository.search;

import com.cetc.cctv.domain.PerimeterProtectRegion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PerimeterProtectRegion entity.
 */
public interface PerimeterProtectRegionSearchRepository extends ElasticsearchRepository<PerimeterProtectRegion, Long> {
}
