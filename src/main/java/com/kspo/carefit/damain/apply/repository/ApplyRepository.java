package com.kspo.carefit.damain.apply.repository;

import com.kspo.carefit.damain.apply.dto.response.ApplyResponse;
import com.kspo.carefit.damain.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply,Long> {

    @Query("""
    select new com.kspo.carefit.damain.apply.dto.response.ApplyResponse(
        c.id,
        c.title,
        c.meetAt
    )
    from Apply a
    join a.carpool c
    join a.user u
    where u.username = :username
""")
    List<ApplyResponse> findApplyDetailsByUsername(String username);

    @Query("""
    select a
    from Apply a
    join a.user u
    join a.carpool c
    where u.username = :username
      and c.id = :postId
""")
    Optional<Apply> findByUsernameAndPostId(String username, Long postId);





}
