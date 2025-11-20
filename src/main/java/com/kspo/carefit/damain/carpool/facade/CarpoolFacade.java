package com.kspo.carefit.damain.carpool.facade;

import com.kspo.carefit.damain.carpool.service.CarpoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarpoolFacade {

    private final CarpoolService carpoolService;


}
