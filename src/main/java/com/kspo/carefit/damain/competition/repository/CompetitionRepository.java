package com.kspo.carefit.damain.competition.repository;

import com.kspo.carefit.damain.competition.entity.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition,Long> {

    Page<Competition> findByItemCodeIn(List<Integer> itemCodes, Pageable pageable);
    Optional<Competition> findById(Long id);

}
