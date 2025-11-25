package com.kspo.carefit.damain.clubs.facade;

import com.kspo.carefit.damain.clubs.dto.response.ClubDetailsResponse;
import com.kspo.carefit.damain.clubs.dto.response.LocalClubsResponse;
import com.kspo.carefit.damain.clubs.service.ClubsService;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ClubsFacade {

    private final ClubsService clubsService;
    private final UserService userService;

    @Transactional
    public Page<LocalClubsResponse> showLocalClubs
            (String username,
             int page,
             int size){

        Integer sigunguCode = userService
                .findByUsername(username)
                .getSigunguCode();

        return clubsService
                .showLocalClubsList(sigunguCode,page,size);

    }

    public ClubDetailsResponse showClubsDetails
            (Long id){

        return clubsService.showClubDetails(id);

    }
}
