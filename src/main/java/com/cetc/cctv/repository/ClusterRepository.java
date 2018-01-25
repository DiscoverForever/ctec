package com.cetc.cctv.repository;

import com.cetc.cctv.domain.Cluster;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cluster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {

}
