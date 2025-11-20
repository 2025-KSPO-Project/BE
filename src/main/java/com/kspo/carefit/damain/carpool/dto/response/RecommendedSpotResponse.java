package com.kspo.carefit.damain.carpool.dto.response;

import java.util.List;

public record RecommendedSpotResponse(Destination destination,
                                      List<Spot> recommendedSpots) {

    public record Destination(Double latitude,
                              Double longitude){

    }

    public record Spot(Double latitude,
                       Double longitude,
                       String placeName,
                       Integer score,
                       Details details){

    }

    public record Details(Double distance,
                          Double elevationRateAvg,
                          Integer distanceRisk,
                          Integer elevationsRisk){

    }

}
