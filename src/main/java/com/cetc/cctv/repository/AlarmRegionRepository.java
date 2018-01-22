package com.cetc.cctv.repository;

import com.cetc.cctv.domain.AlarmRegion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AlarmRegion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlarmRegionRepository extends JpaRepository<AlarmRegion, Long> {

}
