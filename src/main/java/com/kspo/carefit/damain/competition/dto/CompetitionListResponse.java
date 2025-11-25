package com.kspo.carefit.damain.competition.dto;

import java.time.LocalDateTime;

public record CompetitionListResponse(Long id,
                                      String gameNmClean,
                                      LocalDateTime gameBeginDe,
                                      LocalDateTime gameEndDe,
                                      String itemNm) {
}
