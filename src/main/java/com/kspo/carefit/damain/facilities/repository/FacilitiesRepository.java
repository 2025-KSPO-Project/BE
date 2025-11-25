package com.kspo.carefit.damain.facilities.repository;

import com.kspo.carefit.damain.facilities.entity.Facilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities,Long> {

    Page<Facilities> findByDistrictCode(Integer districtCode, Pageable pageable);
    Optional<Facilities> findById(Long id);
}
