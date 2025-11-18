package com.kspo.carefit.damain.user.dto.request;

import java.util.List;

public record UserUpdateCodesRequest (Integer disabilityCode,
                                      Integer sidoCode,
                                      Integer sigunguCode,
                                      List<Integer> sportsCode){
}
