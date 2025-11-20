package com.kspo.carefit.base.helper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MathHelper {

    // 각 지점 사이의 수직거리를 구하는 메소드
    public List<Double> calculateSegmentHeightDifferences(List<Double> elevations) {

        // 지점이 2개 미만인 경우
        if (elevations == null || elevations.size() <= 1) {
            return List.of();
        }

        // 각 구간별 수직거리 계산
        return IntStream.range(0, elevations.size() - 1)
                .mapToObj(i -> {
                    double elevationA = elevations.get(i);
                    double elevationB = elevations.get(i + 1);

                    // 수직거리를 절댓값으로 계산
                    return Math.abs(elevationB - elevationA);
                })
                .collect(Collectors.toList());
    }

    // 경사율을 계산하는 메소드
    public List<Double> calculateElevationRate(List<Double> segmentHeights,
                                               Double distance){

        return segmentHeights.stream()
                .map(heightDiff ->
                        (heightDiff / (distance/29)) * 100) // 총 거리를 구간 총개수 ( 30 - 1 ) 로 나누어 수평거리 계산
                .toList();
    }

    // 좌표 차이로 거리를 구하는 하버사인 거리공식
    public double haversineDistance(double lat1, double lon1,
                           double lat2, double lon2) {

        final double R = 6371000; // 지구 반지름

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // meter 단위
    }


}
