package com.kspo.carefit.damain.carpool.facade;

import com.kspo.carefit.damain.carpool.dto.NearBySpot;
import com.kspo.carefit.damain.carpool.dto.request.GetGradiantRequest;
import com.kspo.carefit.damain.carpool.dto.request.SearchFineWayRequest;
import com.kspo.carefit.damain.carpool.dto.response.RecommendedSpotResponse;
import com.kspo.carefit.damain.carpool.service.CarpoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CarpoolFacade {

    private final CarpoolService carpoolService;

    public NearBySpot findNearBy(SearchFineWayRequest request){

        return carpoolService
                .findNearBy(request.endLat(),
                        request.endLng());

    }

    public RecommendedSpotResponse findRecommendedSpot
            (SearchFineWayRequest request){

        //  위치 5개 추천
        NearBySpot nearBySpot = carpoolService
                .findNearBy(request.endLat(),
                        request.endLng());



       // 추천받은 지점 5개에 대한 경로정보 추출
        List<RecommendedSpotResponse.Spot> recommendedSpots = nearBySpot.
                results()
                .stream()
                .map(result -> {

                    // 1.해발고도 변환 후 , 경사율 반환하기
                    List<Double> elevationRates = carpoolService
                            .getElevationsRateNearBy(carpoolService
                                    .getElevationNearBy(
                                            new GetGradiantRequest(
                                                    result.latitude(),
                                                    result.longitude(),
                                                    request.endLat(),
                                                    request.endLng())),
                                    result.distance());

                    // 2. 평균 경사율 반환
                    Double elevationRateAvg = elevationRates
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                    // 3.위험도 측정
                    Integer elevationRisk = carpoolService.evaluateElevationRisk(elevationRates);
                    Integer distanceRisk = carpoolService.evaluateDistanceRisk(result.distance());


                    // 단일 지점에 대한 정보 추출
                    return new RecommendedSpotResponse.Spot(result.latitude(),
                            result.longitude(), // 추천지점 위도
                            result.placeName(), // 추천지점 경도
                            100- (elevationRisk+distanceRisk), // 위험점수
                            new RecommendedSpotResponse.Details( // Details
                                    result.distance(), // 거리
                                    elevationRateAvg, // 경사율 평균
                                    distanceRisk, // 거리 위험점수
                                    elevationRisk)); // 경사 위험점수

                })
                .toList();

        return new RecommendedSpotResponse(
                new RecommendedSpotResponse.Destination(
                        request.endLat(), request.endLng()),
                recommendedSpots);
    }


}
