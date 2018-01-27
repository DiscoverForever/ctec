package com.cetc.cctv.repository;

import com.cetc.cctv.domain.PerimeterProtectRegion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PerimeterProtectRegion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerimeterProtectRegionRepository extends JpaRepository<PerimeterProtectRegion, Long> {

}
