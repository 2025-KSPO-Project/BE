package com.kspo.carefit.damain.carpool.vo;

import com.kspo.carefit.damain.carpool.dto.request.CarpoolPostRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Spot {

    public Double latitude;
    public Double longitude;
    public String placeName;
    public Integer score;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "spot_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "spot_longitude")),
            @AttributeOverride(name = "placeName", column = @Column(name = "spot_place_name")),
            @AttributeOverride(name = "score", column = @Column(name = "spot_score")),
    })
    public Details details;

}
