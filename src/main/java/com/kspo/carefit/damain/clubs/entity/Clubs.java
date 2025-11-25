package com.kspo.carefit.damain.clubs.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "clubs")
public class Clubs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(name = "club_id", length = 32, nullable = false)
    private String clubId;   // 클럽 고유 ID

    @Column(name = "club_name", length = 255)
    private String clubName; // 클럽명

    @Column(name = "sport_name", length = 128)
    private String sportName; // 종목명

    @Column(name = "sport_subname", length = 128)
    private String sportSubname; // 세부 종목명

    @Column(name = "province_name", length = 50)
    private String provinceName; // 시도명

    @Column(name = "district_name", length = 50)
    private String districtName; // 시군구명

    @Column(name = "disability_type_name", length = 128)
    private String disabilityTypeName; // 장애유형명

    @Column(name = "oper_time_text", length = 255)
    private String operTimeText; // 운영 시간 텍스트

    @Column(name = "intro_text", columnDefinition = "TEXT")
    private String introText; // 소개 문구

    @Column(name = "created_at", length = 50)
    private String createdAt; // 생성일자(문자)

    @Column(name = "updated_at", length = 50)
    private String updatedAt; // 수정일자(문자)

    @Column(name = "disability_type_code", length = 10)
    private String disabilityTypeCode; // 장애유형 코드

    @Column(name = "province_code", length = 10)
    private Integer provinceCode; // 시도코드

    @Column(name = "district_code", length = 10)
    private Integer districtCode; // 시군구코드

    @Column(name = "region_key", length = 50)
    private String regionKey; // province_name + district_name (임의 추가)
}
