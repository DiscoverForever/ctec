package com.cetc.cctv.repository.search;

import com.cetc.cctv.domain.AlarmHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AlarmHistory entity.
 */
public interface AlarmHistorySearchRepository extends ElasticsearchRepository<AlarmHistory, Long> {
}
