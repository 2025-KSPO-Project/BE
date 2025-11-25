package com.kspo.carefit.damain.competition.facade;

import com.kspo.carefit.damain.competition.dto.CompetitionDetailsResponse;
import com.kspo.carefit.damain.competition.dto.CompetitionListResponse;
import com.kspo.carefit.damain.competition.service.CompetitionService;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompetitionFacade {

    private final CompetitionService competitionService;
    private final UserService userService;

    @Transactional
    public Page<CompetitionListResponse> showCompetitions(String username,
                                                          int page,
                                                          int size){

        List<Integer> sportCodes = userService
                .findByUsername(username)
                .getSportsCode();

        return competitionService
                .showCompetitionList(sportCodes,page,size);
    }

    public CompetitionDetailsResponse getCompetitionDetails(Long id){

        return competitionService.showCompetitionDetails(id);

    }
}
