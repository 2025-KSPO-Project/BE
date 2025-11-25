package com.kspo.carefit.damain.clubs.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.clubs.dto.response.ClubDetailsResponse;
import com.kspo.carefit.damain.clubs.dto.response.LocalClubsResponse;
import com.kspo.carefit.damain.clubs.facade.ClubsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubsFacade clubsFacade;

    // 해당 지역 클럽 리스트
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

    // 상세 페이지
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
