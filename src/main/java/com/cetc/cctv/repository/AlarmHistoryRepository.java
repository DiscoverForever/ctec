package com.cetc.cctv.repository;

import com.cetc.cctv.domain.AlarmHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AlarmHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlarmHistoryRepository extends JpaRepository<AlarmHistory, Long> {

}
