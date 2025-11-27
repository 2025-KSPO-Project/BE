package com.kspo.carefit.damain.carpool.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.carpool.dto.request.CarpoolDeleteRequest;
import com.kspo.carefit.damain.carpool.dto.request.CarpoolPostRequest;
import com.kspo.carefit.damain.carpool.dto.request.CheckPosterRequest;
import com.kspo.carefit.damain.carpool.dto.request.SearchFineWayRequest;
import com.kspo.carefit.damain.carpool.dto.response.*;
import com.kspo.carefit.damain.carpool.facade.CarpoolFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "카풀", description = "카풀 관련 API")
@RestController
@RequestMapping("/carpool")
@RequiredArgsConstructor
@Slf4j
public class CarpoolController {

    private final CarpoolFacade carpoolFacade;

    @Operation(summary = "추천 스팟 조회", description = "목표지점 주변 추천 스팟을 조회합니다.")
    @PostMapping("/recommend")
    public ResponseEntity<ApiResult<RecommendedSpotResponse>> recommendFineSpot
            (@RequestBody SearchFineWayRequest searchFineWayRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .findRecommendedSpot(searchFineWayRequest)));
    }

    @Operation(summary = "카풀 생성", description = "새로운 카풀 포스팅을 생성합니다.")
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


    @Operation(summary = "지역 카풀 목록 조회", description = "해당 지역의 카풀 포스팅 목록을 조회합니다.")
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

    @Operation(summary = "카풀 상세 조회", description = "카풀 포스팅 상세 정보를 조회합니다.")
    @GetMapping("/show/post")
    public ResponseEntity<ApiResult<CarpoolResponse>> getPost
            (@RequestParam Long id){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .getCarpoolPost(id)));
    }
    @Operation(summary = "내 카풀 목록 조회", description = "자신이 등록한 카풀 포스팅 목록을 조회합니다.")
    @GetMapping("/show/myposts")
    public ResponseEntity<ApiResult<MyCarpoolResponse>> getMyCarpoolPost
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .getMyCarpool(userDetails.getUsername())));
    }

    @Operation(summary = "카풀 작성자와 일치여부 조회",description = "사용자와 카풀 작성자가 일치하는지 조회합니다.")
    @GetMapping("/check-poster")
    public ResponseEntity<ApiResult<CheckPosterResponse>> checkPoster
            (@RequestBody CheckPosterRequest checkPosterRequest,
             @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(carpoolFacade
                                .checkPoster(
                                        checkPosterRequest,
                                        userDetails.getUsername())));
    }

    @Operation(summary = "카풀 삭제", description = "카풀 포스팅을 삭제합니다.")
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
