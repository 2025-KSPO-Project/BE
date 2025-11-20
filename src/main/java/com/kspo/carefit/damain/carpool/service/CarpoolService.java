package com.kspo.carefit.damain.carpool.service;

import com.kspo.carefit.base.client.GoogleMapClient;
import com.kspo.carefit.base.helper.MathHelper;
import com.kspo.carefit.damain.carpool.dto.NearBySpot;
import com.kspo.carefit.damain.carpool.dto.request.GetGradiantRequest;
import com.kspo.carefit.damain.carpool.dto.response.GetGradiantResponse;
import com.kspo.carefit.damain.carpool.dto.response.NearByResponse;
import com.kspo.carefit.damain.carpool.repository.CarpoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarpoolService {

    private final CarpoolRepository carpoolRepository;
    private final GoogleMapClient googleMapClient;
    private final MathHelper mathHelper;

    public NearBySpot findNearBy (Double lat,
                      Double lng){

        NearByResponse nearByResponse = googleMapClient.searchNearby(lat,lng);

        return new NearBySpot(
                nearByResponse.places()
                        .stream()
                        .map(place -> new NearBySpot.Result(
                                place.location().latitude(),
                                place.location().longitude(),
                                place.displayName().text(),
                                mathHelper
                                        .haversineDistance(lat,lng,
                                                place.location().latitude(),
                                                place.location().longitude())
                        ))
                        .toList()
        );

    }

    // 추천지점 - 도착지점 사이의 30개 지점들의 해발고도를 반환하는 메소드
    public List<Double> getElevationNearBy
    (GetGradiantRequest getGradiantRequest){

        GetGradiantResponse getGradiantResponse = googleMapClient
                .getGradiantNearBy(getGradiantRequest.startLat(),
                        getGradiantRequest.startLng(),
                        getGradiantRequest.endLat(),
                        getGradiantRequest.endLng());

        return getGradiantResponse
                .results()
                .stream()
                .map(GetGradiantResponse.results::elevation)
                .toList();

    }

    // 인근 지점간의 경사율을 구하는 메소드
    public List<Double> getElevationsRateNearBy(List<Double> elevations,Double distance){

        return mathHelper
                .calculateElevationRate(mathHelper
                        .calculateSegmentHeightDifferences(elevations),distance);
    }

    // 경사 위험도를 구하는 메소드
    public Integer evaluateElevationRisk(List<Double> elevationRates){

        return elevationRates
                .stream()
                .mapToInt(rate ->{
                    if(rate<=5) return 0;
                    if((rate > 5)&&(rate <=8)) return 1;
                    if(rate>8) return 2;
                    return 2;
                })
                .sum();


    }

    // 거리 위험도를 구하는 메소드
    public Integer evaluateDistanceRisk(Double distance){
        return (distance.intValue())/10;
    }


}
