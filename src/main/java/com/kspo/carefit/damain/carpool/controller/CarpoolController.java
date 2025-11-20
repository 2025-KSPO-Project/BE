package com.kspo.carefit.damain.carpool.controller;

import com.kspo.carefit.base.client.GoogleMapClient;
import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.carpool.dto.request.SearchFineWayRequest;
import com.kspo.carefit.damain.carpool.dto.response.NearByResponse;
import com.kspo.carefit.damain.carpool.facade.CarpoolFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carpool")
@RequiredArgsConstructor
public class CarpoolController {

    private final CarpoolFacade carpoolFacade;
    private final GoogleMapClient testClient;

    // NearBy 테스트
    @PostMapping("/test/nearby")
    public ResponseEntity<ApiResult<NearByResponse>> testNearBy
            (@RequestBody SearchFineWayRequest searchFineWayRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(testClient
                                .searchNearby(searchFineWayRequest.finishLat(),
                                        searchFineWayRequest.finishLng())));

    }

    public ResponseEntity<ApiResult<?>> testGetGradiant;

}
