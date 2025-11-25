package com.kspo.carefit.damain.apply.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.apply.dto.request.AddApplyRequest;
import com.kspo.carefit.damain.apply.dto.request.DeleteApplyRequest;
import com.kspo.carefit.damain.apply.dto.request.ShowApplyRequest;
import com.kspo.carefit.damain.apply.dto.response.AddApplyResponse;
import com.kspo.carefit.damain.apply.dto.response.DeleteApplyResponse;
import com.kspo.carefit.damain.apply.dto.response.ShowApplyResponse;
import com.kspo.carefit.damain.apply.facade.ApplyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyFacade applyFacade;

    // 지원 등록하기
    @PostMapping("/add")
    public ResponseEntity<ApiResult<AddApplyResponse>> add
            (@RequestBody AddApplyRequest addApplyRequest){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(applyFacade
                                .createApply(addApplyRequest)));

    }

    // 지원 취소하기
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResult<DeleteApplyResponse>> delete
            (@RequestBody DeleteApplyRequest deleteApplyRequest){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(applyFacade
                                .deleteApply(deleteApplyRequest)));

    }

    // 자신이 지원한 카풀 보여주기
    @GetMapping("/show")
    public ResponseEntity<ApiResult<ShowApplyResponse>> showApply
            (@RequestBody ShowApplyRequest showApplyRequest){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(applyFacade
                                .showApply(showApplyRequest)));

    }

}
