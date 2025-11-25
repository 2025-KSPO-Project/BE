package com.kspo.carefit.damain.clubs.repository;

import com.kspo.carefit.damain.clubs.entity.Clubs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubsRepository extends JpaRepository<Clubs,Long> {

    Page<Clubs> findByDistrictCode(Integer districtCode,Pageable pageable);

    Optional<Clubs> findById(Long id);


}
