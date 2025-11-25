package com.kspo.carefit.damain.carpool.dto.response;

import com.kspo.carefit.damain.carpool.entity.Carpool;

import java.util.List;

public record CarpoolResponse(String writer,
                              String title,
                              String content,
                              Route route,
                              MiddleSpot middleSpot,
                              Integer applyCount,
                              Integer maxCount) {

    public record Route(Double start_lat,
                        Double start_lng,
                        Double end_lat,
                        Double end_lng){

    }

    public record MiddleSpot(Double middle_lat,
                             Double middle_lng,
                             Integer score,
                             Double distance){

    }

    public static Route getRouteFromCarpool(Carpool carpool){

        return new Route(
                carpool.getStart().getLatitude(),
                carpool.getStart().getLongitude(),
                carpool.getDestination().getLatitude(),
                carpool.getDestination().getLongitude());
    }

    public static MiddleSpot getMiddleSpotFromCarpool(Carpool carpool){

        return new MiddleSpot(
                carpool.getSpot().getLatitude(),
                carpool.getSpot().getLongitude(),
                carpool.getSpot().getScore(),
                carpool.getSpot().getDetails().getDistance()
        );
    }
}
