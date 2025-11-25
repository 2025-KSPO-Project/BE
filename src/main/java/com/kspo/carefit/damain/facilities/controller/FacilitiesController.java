package com.kspo.carefit.damain.facilities.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.facilities.dto.response.FacilityDetailsResponse;
import com.kspo.carefit.damain.facilities.dto.response.LocalFacilitiesResponse;
import com.kspo.carefit.damain.facilities.facade.FacilitiesFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilitiesController {

    private final FacilitiesFacade facilitiesFacade;

    @GetMapping("/list/local")
    public ResponseEntity<ApiResult<Page<LocalFacilitiesResponse>>> showLocalFacilities
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestParam int page,
             @RequestParam int size){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(facilitiesFacade
                                .showLocalFacilities(
                                        userDetails.getUsername(),
                                        page,
                                        size)));

    }

    @GetMapping("/details")
    public ResponseEntity<ApiResult<FacilityDetailsResponse>> details
            (@RequestParam Long id){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(facilitiesFacade
                                .showFacilityDetails(id)));
    }
}
