package com.kspo.carefit.damain.clubs.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.clubs.dto.response.ClubDetailsResponse;
import com.kspo.carefit.damain.clubs.dto.response.LocalClubsResponse;
import com.kspo.carefit.damain.clubs.entity.Clubs;
import com.kspo.carefit.damain.clubs.repository.ClubsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubsService {

    private final ClubsRepository clubsRepository;


    public Page<LocalClubsResponse> showLocalClubsList(Integer sigunguCode,
                                                   int page,
                                                   int size){

        Page<Clubs> result = clubsRepository
                .findByDistrictCode(
                        sigunguCode,
                        PageRequest.of(page,size));

        return result.map(clubs->
                new LocalClubsResponse(
                        clubs.getId(),
                        clubs.getClubName(),
                        clubs.getRegionKey(),
                        clubs.getSportName())
        );

    }


    public ClubDetailsResponse showClubDetails
            (Long id){

        Clubs clubs = findById(id);

        return new ClubDetailsResponse(
                clubs.getId(),
                clubs.getClubName(),
                clubs.getRegionKey(),
                clubs.getSportName(),
                clubs.getSportSubname(),
                clubs.getIntroText());
    }


    /*
    기본 CRUD 모음
     */
    public Clubs findById(Long id){
        return clubsRepository
                .findById(id)
                .orElseThrow(()-> new BaseException(BaseExceptionEnum.CLUBS_NOT_FOUND));
    }
}
