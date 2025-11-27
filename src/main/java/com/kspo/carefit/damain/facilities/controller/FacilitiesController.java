package com.kspo.carefit.damain.facilities.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.facilities.dto.response.FacilityDetailsResponse;
import com.kspo.carefit.damain.facilities.dto.response.LocalFacilitiesResponse;
import com.kspo.carefit.damain.facilities.facade.FacilitiesFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "시설", description = "시설 관련 API")
@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilitiesController {

    private final FacilitiesFacade facilitiesFacade;

    @Operation(summary = "지역 시설 목록 조회", description = "해당 지역의 시설 목록을 조회합니다.")
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

    @Operation(summary = "시설 상세 조회", description = "시설 상세 정보를 조회합니다.")
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
