package com.kspo.carefit.damain.user.dto.response;

import java.util.List;

public record UserUpdateCodesResponse(Integer disabilityCode,
                                      Integer sidoCode,
                                      Integer sigunguCode,
                                      List<Integer> sportsCode) {
}
