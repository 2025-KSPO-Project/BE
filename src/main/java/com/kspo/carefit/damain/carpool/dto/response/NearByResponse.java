package com.kspo.carefit.damain.carpool.dto.response;


import java.util.List;

public record NearByResponse(List<Place> places) {

    public record Place(List<String> types,
                        Location location,
                        DisplayName displayName) {

    }

    public record Location(double latitude,
                           double longitude) {

    }

    public record DisplayName(String text,
                              String languageCode) {

    }
}