package com.kspo.carefit.damain.clubs.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.clubs.dto.response.ClubDetailsResponse;
import com.kspo.carefit.damain.clubs.dto.response.LocalClubsResponse;
import com.kspo.carefit.damain.clubs.facade.ClubsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "클럽", description = "클럽 관련 API")
@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubsFacade clubsFacade;

    @Operation(summary = "지역 클럽 목록 조회", description = "해당 지역의 클럽 목록을 조회합니다.")
    @GetMapping("/list/local")
    public ResponseEntity<ApiResult<Page<LocalClubsResponse>>> showLocalClubs
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestParam int page,
             @RequestParam int size){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(clubsFacade
                                .showLocalClubs(
                                        userDetails.getUsername(),
                                        page,
                                        size)));
    }

    @Operation(summary = "클럽 상세 조회", description = "클럽 상세 정보를 조회합니다.")
    @GetMapping("/details")
    public ResponseEntity<ApiResult<ClubDetailsResponse>> details
            (@RequestParam Long id){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(clubsFacade
                                .showClubsDetails(id)));

    }

}
