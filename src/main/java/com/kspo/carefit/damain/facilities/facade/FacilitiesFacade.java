package com.kspo.carefit.damain.facilities.facade;

import com.kspo.carefit.damain.facilities.dto.response.FacilityDetailsResponse;
import com.kspo.carefit.damain.facilities.dto.response.LocalFacilitiesResponse;
import com.kspo.carefit.damain.facilities.service.FacilitiesService;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FacilitiesFacade {

    private final FacilitiesService facilitiesService;
    private final UserService userService;

    @Transactional
    public Page<LocalFacilitiesResponse> showLocalFacilities(String username,
                                                             int page,
                                                             int size){

        Integer sigunguCode = userService
                .findByUsername(username)
                .getSigunguCode();

        return facilitiesService.showLocalFacilities(sigunguCode,page,size);

    }

    public FacilityDetailsResponse showFacilityDetails(Long id){
        return facilitiesService.showFacilityDetails(id);
    }
}
