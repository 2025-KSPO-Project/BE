package com.kspo.carefit.damain.facilities.dto.response;

public record LocalFacilitiesResponse(Long id,
                                      String facilityName,
                                      String provinceName,
                                      String districtName,
                                      String address,
                                      String mainSportName) {
}
