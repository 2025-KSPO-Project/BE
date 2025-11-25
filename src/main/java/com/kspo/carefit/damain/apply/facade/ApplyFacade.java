package com.kspo.carefit.damain.apply.facade;

import com.kspo.carefit.damain.apply.dto.request.AddApplyRequest;
import com.kspo.carefit.damain.apply.dto.request.DeleteApplyRequest;
import com.kspo.carefit.damain.apply.dto.request.ShowApplyRequest;
import com.kspo.carefit.damain.apply.dto.response.AddApplyResponse;
import com.kspo.carefit.damain.apply.dto.response.DeleteApplyResponse;
import com.kspo.carefit.damain.apply.dto.response.ShowApplyResponse;
import com.kspo.carefit.damain.apply.entity.Apply;
import com.kspo.carefit.damain.apply.service.ApplyService;
import com.kspo.carefit.damain.carpool.entity.Carpool;
import com.kspo.carefit.damain.carpool.service.CarpoolService;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ApplyFacade {

    private final ApplyService applyService;
    private final UserService userService;
    private final CarpoolService carpoolService;

    @Transactional
    public AddApplyResponse createApply(AddApplyRequest addApplyRequest){

        User user = userService
                .findByUsername(addApplyRequest
                        .applierUsername());

        Carpool carpool = carpoolService.findById(addApplyRequest.postId());

        Apply apply = applyService.makeApplyEntity();

        user.addApply(apply);
        carpool.addApply(apply);

        applyService.saveApply(apply);

        return new AddApplyResponse(
                addApplyRequest.postId(),
                user.getNickname(),
                "카풀 등록이 완료되었습니다.");

    }

    @Transactional
    public ShowApplyResponse showApply(ShowApplyRequest showApplyRequest){
        return applyService.showApply(showApplyRequest);
    }

    @Transactional
    public DeleteApplyResponse deleteApply(DeleteApplyRequest deleteApplyRequest){

        Carpool carpool = carpoolService
                .findById(deleteApplyRequest.postId());

        User user = userService
                .findByUsername(deleteApplyRequest.applierUsername());

        Apply apply = applyService
                .findApplyByUsernameAndPostId(
                        deleteApplyRequest.applierUsername(),
                        deleteApplyRequest.postId());

        carpool.deleteApply(apply);
        user.deleteApply(apply);

        applyService.deleteApply(apply);

        return new DeleteApplyResponse(
                deleteApplyRequest.postId(),
                user.getNickname(),
                "해당 지원이 취소되었습니다.");

    }
}
