package com.kspo.carefit.damain.carpool.dto;

import java.util.List;

public record NearBySpot(List<Result> results) {

    public record Result(Double latitude,
                         Double longitude,
                         String placeName,
                         Double distance) {

    }
}
