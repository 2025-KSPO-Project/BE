package com.kspo.carefit.damain.carpool.repository;

import com.kspo.carefit.damain.carpool.entity.Carpool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarpoolRepository extends JpaRepository<Carpool,Long> {

}
