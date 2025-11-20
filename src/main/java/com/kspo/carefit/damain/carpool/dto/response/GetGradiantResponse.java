package com.kspo.carefit.damain.carpool.dto.response;

import java.util.List;

public record GetGradiantResponse(List<results> results) {

    public record results(Double elevation,
                          Location location,
                          Double resolution){

    }

    public record Location(Double lat,
                           Double lng){

    }
}
