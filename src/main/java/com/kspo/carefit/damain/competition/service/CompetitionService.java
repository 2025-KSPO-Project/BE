package com.kspo.carefit.damain.competition.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.competition.dto.CompetitionDetailsResponse;
import com.kspo.carefit.damain.competition.dto.CompetitionListResponse;
import com.kspo.carefit.damain.competition.entity.Competition;
import com.kspo.carefit.damain.competition.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    public Page<CompetitionListResponse> showCompetitionList(List<Integer> itemCodes,
                                                         int page,
                                                         int size){

        Page<Competition> results = competitionRepository
                .findByItemCodeIn(
                        itemCodes,
                        PageRequest.of(page,size));

        return results.map(competition ->
                new CompetitionListResponse(
                        competition.getId(),
                        competition.getGameNmClean(),
                        competition.getGameBeginDe(),
                        competition.getGameEndDe(),
                        competition.getItemNm()));
    }

    public CompetitionDetailsResponse showCompetitionDetails(Long id){

        Competition competition = findById(id);

        return new CompetitionDetailsResponse(
                competition.getId(),
                competition.getGameNmClean(),
                competition.getGameBeginDe(),
                competition.getGameEndDe(),
                competition.getItemNm(),
                competition.getSubitemNm(),
                competition.getOparNm(),
                competition.getHomepageUrl());
    }

    /*
    기본 CRUD 모음
     */

    public Competition findById(Long id){
        return competitionRepository
                .findById(id)
                .orElseThrow(()->
                        new BaseException(BaseExceptionEnum.COMPETITION_NOT_FOUND));
    }


}
