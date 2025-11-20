package com.kspo.carefit.base.client;

import com.kspo.carefit.base.helper.GoogleHelper;
import com.kspo.carefit.damain.carpool.dto.request.NearBySearchRequest;
import com.kspo.carefit.damain.carpool.dto.response.GetGradiantResponse;
import com.kspo.carefit.damain.carpool.dto.response.NearByResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleMapClient {

    private final RestTemplate restTemplate;

    @Value("${google.key}")
    private String key;

    // 주변 찾기 메소드
    public NearByResponse searchNearby(Double lat,
                               Double lng){

        // 요청 바디 설정
        NearBySearchRequest nearBySearchRequest = NearBySearchRequest.builder()
                .includedTypes(List.of("parking","bus_stop","taxi_stand"))
                .maxResultCount(5)
                .languageCode("Ko")
                .rankPreference("DISTANCE")
                .locationRestriction(
                        new NearBySearchRequest.LocationRestriction(
                                new NearBySearchRequest.Circle(
                                        new NearBySearchRequest.Center(lat,lng),
                                        200.0
                                )
                        )
                )
                .build();

        // 헤더 설정
        HttpHeaders httpHeaders = GoogleHelper
                .googleHeaders(key,"places.displayName," +
                        "places.location," +
                        "places.types," +
                        "places.accessibilityOptions.wheelchairAccessibleParking");

        HttpEntity<?> entity = new HttpEntity<>(nearBySearchRequest,httpHeaders);

        ResponseEntity<NearByResponse> response =
                restTemplate.exchange(
                        "https://places.googleapis.com/v1/places:searchNearby",
                        HttpMethod.POST,
                        entity,
                        NearByResponse.class
                );

        return response.getBody();

    }

    // 출발 - 도착지점 사이의 경사도 구하기 메소드
    public GetGradiantResponse getGradiantNearBy(Double startLat, Double startLng,
                                                 Double endLat, Double endLng){

        String url = "https://maps.googleapis.com/maps/api/elevation/json"
                + "?path=%s,%s|%s,%s&samples=%d&key=%s";

        String finalUrl = String.format(url,
                startLat, startLng,
                endLat, endLng,
                30,
                key
        );

        ResponseEntity<GetGradiantResponse> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                GetGradiantResponse.class
        );

        return response.getBody();
    }


}
