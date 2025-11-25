package com.kspo.carefit.damain.carpool.dto.request;

import java.time.Instant;

public record CarpoolPostRequest(String title,
                                 String content,
                                 Integer maxCount,
                                 Instant meetAt,
                                 Start start,
                                 Destination destination,
                                 Spot spot) {


    public record Start(Double latitude,
                        Double longitude){

    }

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

    public com.kspo.carefit.damain.carpool.vo.Start toStart(){
        return new com.kspo.carefit.damain.carpool.vo.Start(
                start.latitude(),
                start.longitude());
    }

    // DTO 의 필드를 엔티티 필드로 매핑

    // Destination 필드
    public com.kspo.carefit.damain.carpool.vo.Destination toDestination(){
        return new com.kspo.carefit.damain.carpool.vo.Destination(destination.latitude(),destination.longitude());
    }

    // Spot 필드
    public com.kspo.carefit.damain.carpool.vo.Spot toSpot(){
        return new com.kspo.carefit.damain.carpool.vo.Spot(
                spot.latitude(),
                spot.longitude(),
                spot.placeName(),
                spot.score(),
                toDetails());
    }

    // Details 필드
    public com.kspo.carefit.damain.carpool.vo.Details toDetails(){
        return new com.kspo.carefit.damain.carpool.vo.Details(
                spot.details.distance(),
                spot.details.elevationRateAvg(),
                spot.details.distanceRisk(),
                spot.details.elevationsRisk());
    }
}
