package com.kspo.carefit.damain.carpool.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.carpool.dto.request.CarpoolDeleteRequest;
import com.kspo.carefit.damain.carpool.dto.request.CarpoolPostRequest;
import com.kspo.carefit.damain.carpool.dto.request.SearchFineWayRequest;
import com.kspo.carefit.damain.carpool.dto.response.*;
import com.kspo.carefit.damain.carpool.facade.CarpoolFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carpool")
@RequiredArgsConstructor
@Slf4j
public class CarpoolController {

    private final CarpoolFacade carpoolFacade;

    // 목표지점 주변 스팟찾기
    @PostMapping("/recommend")
    public ResponseEntity<ApiResult<RecommendedSpotResponse>> recommendFineSpot
            (@RequestBody SearchFineWayRequest searchFineWayRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .findRecommendedSpot(searchFineWayRequest)));
    }

    // 카풀 포스팅 생성하기
    @PostMapping("/create")
    public ResponseEntity<ApiResult<CarpoolPostResponse>> createCarpool
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody CarpoolPostRequest carpoolPostRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(carpoolFacade
                        .postCarpool(
                                carpoolPostRequest,
                                userDetails.getUsername())));

    }


    // 해당 지역의 카풀 포스팅 보여주기
    @GetMapping("/show/posts/local")
    public ResponseEntity<ApiResult<Page<CarpoolResponse>>> getLocalCarpools
            (@RequestParam int page,
             @RequestParam int size,
             @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .getLocalCarpoolPost(
                                        userDetails.getUsername(),
                                        page,
                                        size)));
    }

    // 카풀 포스팅 보기
    @GetMapping("/show/post")
    public ResponseEntity<ApiResult<CarpoolResponse>> getPost
            (@RequestParam Long id){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .getCarpoolPost(id)));
    }
    // 자신의 카풀 포스팅 가져오기
    @GetMapping("/show/myposts")
    public ResponseEntity<ApiResult<MyCarpoolResponse>> getMyCarpoolPost
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .getMyCarpool(userDetails.getUsername())));
    }

    // 카풀 포스팅 삭제하기
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResult<CarpoolDeleteResponse>> deleteCarpool
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody CarpoolDeleteRequest carpoolDeleteRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade.deleteCarpool(
                                carpoolDeleteRequest.id(),
                                userDetails.getUsername())));

    }

}
