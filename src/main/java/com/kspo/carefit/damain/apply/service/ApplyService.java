package com.kspo.carefit.damain.apply.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.apply.dto.request.ShowApplyRequest;
import com.kspo.carefit.damain.apply.dto.response.ApplyResponse;
import com.kspo.carefit.damain.apply.dto.response.ShowApplyResponse;
import com.kspo.carefit.damain.apply.entity.Apply;
import com.kspo.carefit.damain.apply.repository.ApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;

    public Apply makeApplyEntity(){

        return Apply
                .builder()
                .applyAt(Instant.now())
                .build();

    }

    /*
    기본 CRUD 모음
     */

    // Apply 저장
    public void saveApply(Apply apply){
        applyRepository.save(apply);
    }

    // Apply 삭제
    public void deleteApply(Apply apply){
        applyRepository.delete(apply);
    }

    // Apply 보여주기
    public ShowApplyResponse showApply
            (ShowApplyRequest showApplyRequest){

        return new ShowApplyResponse(applyRepository
                .findApplyDetailsByUsername(showApplyRequest.username()));
    }

    // Apply 객체를 username 과 postId 를 통해 가져오기
    public Apply findApplyByUsernameAndPostId(String username,Long postId){

        return applyRepository
                .findByUsernameAndPostId(username,postId)
                .orElseThrow(()->
                        new BaseException(BaseExceptionEnum.APPLY_NOT_FOUND));

    }


}
