package com.kspo.carefit.damain.facilities.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "facilities")
public class Facilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // bigint

    @Column(name = "facility_name", length = 255)
    private String facilityName;

    @Column(name = "main_sport_code", length = 32)
    private Integer mainSportCode;

    @Column(name = "main_sport_name", length = 128)
    private String mainSportName;

    @Column(name = "province_code")
    private Integer provinceCode;

    @Column(name = "province_name", length = 50)
    private String provinceName;

    @Column(name = "district_code")
    private Integer districtCode;

    @Column(name = "district_name", length = 50)
    private String districtName;

    @Column(name = "postal_code", length = 8)
    private String postalCode;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "address_detail", length = 300)
    private String addressDetail;

    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal latitude;  // decimal(10,6)

    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal longitude; // decimal(10,6)

    @Column(name = "course_type_code", length = 32)
    private String courseTypeCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
