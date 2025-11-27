package com.kspo.carefit.damain.competition.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.competition.dto.CompetitionDetailsResponse;
import com.kspo.carefit.damain.competition.dto.CompetitionListResponse;
import com.kspo.carefit.damain.competition.facade.CompetitionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
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

@Tag(name = "대회", description = "대회 관련 API")
@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionFacade competitionFacade;

    @Operation(summary = "대회 목록 조회", description = "대회 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResult<Page<CompetitionListResponse>>> showCompetitions
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestParam int page,
             @RequestParam int size){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(competitionFacade
                                .showCompetitions(
                                        userDetails.getUsername(),
                                        page,
                                        size)));
    }

    @Operation(summary = "대회 상세 조회", description = "대회 상세 정보를 조회합니다.")
    @GetMapping("/details")
    public ResponseEntity<ApiResult<CompetitionDetailsResponse>> details
            (@RequestParam Long id){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(competitionFacade
                                .getCompetitionDetails(id)));

    }


}
