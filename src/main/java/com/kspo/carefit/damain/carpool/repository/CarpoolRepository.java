package com.kspo.carefit.damain.carpool.repository;

import com.kspo.carefit.damain.carpool.entity.Carpool;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

@Repository
public interface CarpoolRepository extends JpaRepository<Carpool,Long> {

    Optional<Carpool> findById(Long id);
    List<Carpool> findByWriterId(Long id);

    Page<Carpool> findByWriter_SigunguCode(Integer sigunguCode,
                                           Pageable pageable);

    @Query("""
select c from Carpool c
join fetch c.writer
where c.id = :id
""")
    Optional<Carpool> findByIdWithWriter(Long id);

}
