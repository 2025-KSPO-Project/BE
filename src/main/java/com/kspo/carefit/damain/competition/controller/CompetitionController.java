package com.kspo.carefit.damain.competition.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.competition.dto.CompetitionDetailsResponse;
import com.kspo.carefit.damain.competition.dto.CompetitionListResponse;
import com.kspo.carefit.damain.competition.facade.CompetitionFacade;
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

@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionFacade competitionFacade;

    // 대회 리스트 가져오기
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
