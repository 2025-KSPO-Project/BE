package com.kspo.carefit.base.security.auth.repository;

import com.kspo.carefit.base.security.auth.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<Refresh,Long> {

    void deleteByRefreshToken(String refreshToken);

}
