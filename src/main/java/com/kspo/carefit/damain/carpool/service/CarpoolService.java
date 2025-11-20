package com.kspo.carefit.damain.carpool.service;

import com.kspo.carefit.base.client.GoogleMapClient;
import com.kspo.carefit.damain.carpool.dto.NearBySpot;
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

    public List<NearBySpot> findNearBy (Double lat,
                      Double lng){

        NearByResponse nearByResponse = googleMapClient.searchNearby(lat,lng);

        return nearByResponse
                .places()
                .stream()
                .map(place -> new NearBySpot(
                        place.location().latitude(),
                        place.location().longitude()
                ))
                .toList();

    }

    public void getGradient(List<NearBySpot> spots){

        /*
        List<String> results = spots.stream()

         */



    }






}
