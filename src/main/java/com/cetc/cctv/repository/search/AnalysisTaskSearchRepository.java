package com.cetc.cctv.repository.search;

import com.cetc.cctv.domain.AnalysisTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AnalysisTask entity.
 */
public interface AnalysisTaskSearchRepository extends ElasticsearchRepository<AnalysisTask, Long> {
}
