package com.kspo.carefit.damain.carpool.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Details {

    public Double distance;
    public Double elevationRateAvg;
    public Integer distanceRisk;
    public Integer elevationsRisk;


}
