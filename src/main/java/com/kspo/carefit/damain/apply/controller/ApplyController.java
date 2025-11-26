package com.kspo.carefit.damain.apply.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.damain.apply.dto.request.AddApplyRequest;
import com.kspo.carefit.damain.apply.dto.request.DeleteApplyRequest;
import com.kspo.carefit.damain.apply.dto.request.ShowApplyRequest;
import com.kspo.carefit.damain.apply.dto.response.AddApplyResponse;
import com.kspo.carefit.damain.apply.dto.response.DeleteApplyResponse;
import com.kspo.carefit.damain.apply.dto.response.ShowApplyResponse;
import com.kspo.carefit.damain.apply.facade.ApplyFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "카풀 지원", description = "카풀 지원 관련 API")
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyFacade applyFacade;

    @Operation(summary = "지원 등록", description = "카풀에 지원을 등록합니다.")
    @PostMapping("/add")
    public ResponseEntity<ApiResult<AddApplyResponse>> add
            (@RequestBody AddApplyRequest addApplyRequest){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(applyFacade
                                .createApply(addApplyRequest)));

    }

    @Operation(summary = "지원 취소", description = "카풀 지원을 취소합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResult<DeleteApplyResponse>> delete
            (@RequestBody DeleteApplyRequest deleteApplyRequest){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(applyFacade
                                .deleteApply(deleteApplyRequest)));

    }

    @Operation(summary = "지원 목록 조회", description = "자신이 지원한 카풀 목록을 조회합니다.")
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
