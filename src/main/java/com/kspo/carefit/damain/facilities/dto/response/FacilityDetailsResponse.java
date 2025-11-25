package com.kspo.carefit.damain.facilities.dto.response;

import java.math.BigDecimal;

public record FacilityDetailsResponse(Long id,
                                      String facilityName,
                                      String provinceName,
                                      String districtName,
                                      String address,
                                      String mainSportName,
                                      BigDecimal latitude,
                                      BigDecimal longitude) {
}
