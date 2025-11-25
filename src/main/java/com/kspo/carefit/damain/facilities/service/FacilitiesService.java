package com.kspo.carefit.damain.facilities.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.facilities.dto.response.FacilityDetailsResponse;
import com.kspo.carefit.damain.facilities.dto.response.LocalFacilitiesResponse;
import com.kspo.carefit.damain.facilities.entity.Facilities;
import com.kspo.carefit.damain.facilities.repository.FacilitiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacilitiesService {

    private final FacilitiesRepository facilitiesRepository;

    public Page<LocalFacilitiesResponse> showLocalFacilities(Integer sigunguCode,
                                                             int page,
                                                             int size){

        Page<Facilities> results = facilitiesRepository
                .findByDistrictCode(
                        sigunguCode,
                        PageRequest.of(
                                page,
                                size));

        return results.map(facilities ->
                new LocalFacilitiesResponse(
                        facilities.getId(),
                        facilities.getFacilityName(),
                        facilities.getProvinceName(),
                        facilities.getDistrictName(),
                        facilities.getAddress(),
                        facilities.getMainSportName()));
    }

    public FacilityDetailsResponse showFacilityDetails(Long id){

        Facilities facilities = findById(id);

        return new FacilityDetailsResponse(
                facilities.getId(),
                facilities.getFacilityName(),
                facilities.getProvinceName(),
                facilities.getDistrictName(),
                facilities.getAddress(),
                facilities.getMainSportName(),
                facilities.getLatitude(),
                facilities.getLongitude()
        );
    }

    /*
    기본 CRUD 모음
     */

    public Facilities findById(Long id){
        return facilitiesRepository
                .findById(id)
                .orElseThrow(()->
                        new BaseException(BaseExceptionEnum.FACILITY_NOT_FOUND));
    }
}
