package com.kspo.carefit.damain.carpool.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record NearBySearchRequest(List<String> includedTypes,
                                  Integer maxResultCount,
                                  String languageCode,
                                  String rankPreference,
                                  LocationRestriction locationRestriction) {

    public record LocationRestriction(Circle circle){

    }

    public record Circle(Center center,
                         Double radius){

    }

    public record Center(Double latitude,
                         Double longitude){

    }
}
