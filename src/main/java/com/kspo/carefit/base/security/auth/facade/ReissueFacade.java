package com.kspo.carefit.base.security.auth.facade;

import com.kspo.carefit.base.security.auth.dto.response.ReissueResponse;
import com.kspo.carefit.base.security.auth.service.ReissueService;
import com.kspo.carefit.base.security.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReissueFacade {

    private final ReissueService reissueService;
    private final CookieUtil cookieUtil;

    @Transactional
    public ReissueResponse refreshTokens(String refresh){

        return reissueService
                .reissue(refresh);

    }

    @Transactional
    public void deleteRefresh(String refresh){
        reissueService.deleteRefresh(refresh);
    }

}
