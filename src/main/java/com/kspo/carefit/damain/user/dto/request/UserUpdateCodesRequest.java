package com.kspo.carefit.damain.user.dto.request;

public record UserUpdateCodesRequest (String username,
                                      Integer disabilityCode,
                                      Integer sidoCode,
                                      Integer sigunguCode){
}
