package com.cetc.cctv.repository.search;

import com.cetc.cctv.domain.Camera;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Camera entity.
 */
public interface CameraSearchRepository extends ElasticsearchRepository<Camera, Long> {
}
