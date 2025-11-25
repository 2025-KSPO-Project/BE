package com.kspo.carefit.damain.clubs.dto.response;

public record ClubDetailsResponse(Long id,
                                  String clubName,
                                  String regionKey,
                                  String sportName,
                                  String sportSubname,
                                  String introText) {
}
