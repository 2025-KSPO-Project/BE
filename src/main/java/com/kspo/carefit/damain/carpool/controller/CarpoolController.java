package com.kspo.carefit.damain.carpool.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.carpool.dto.request.SearchFineWayRequest;
import com.kspo.carefit.damain.carpool.dto.response.RecommendedSpotResponse;
import com.kspo.carefit.damain.carpool.facade.CarpoolFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carpool")
@RequiredArgsConstructor
public class CarpoolController {

    private final CarpoolFacade carpoolFacade;

    @PostMapping("/recommend")
    public ResponseEntity<ApiResult<RecommendedSpotResponse>> recommendFineSpot
            (@RequestBody SearchFineWayRequest searchFineWayRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .findRecommendedSpot(searchFineWayRequest)));
    }

}
