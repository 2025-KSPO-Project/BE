package com.kspo.carefit.damain.competition.dto;

import java.time.LocalDateTime;

public record CompetitionDetailsResponse(Long id,
                                         String gameNmClean,
                                         LocalDateTime gameBeginDe,
                                         LocalDateTime gameEndDe,
                                         String itemNm,
                                         String subitemNm,
                                         String oparNm,
                                         String hmpgUrl) {
}
