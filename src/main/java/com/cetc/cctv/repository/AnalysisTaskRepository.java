package com.cetc.cctv.repository;

import com.cetc.cctv.domain.AnalysisTask;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AnalysisTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalysisTaskRepository extends JpaRepository<AnalysisTask, Long> {

}
