package com.kspo.carefit.damain.competition.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "competition")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_nm_clean", length = 200)
    private String gameNmClean;

    @Column(name = "year")
    private Short year;   // smallint → Short 사용

    @Column(name = "item_nm", length = 50)
    private String itemNm;

    @Column(name = "subitem_nm", length = 50)
    private String subitemNm;

    @Column(name = "asoc_nm", length = 100)
    private String asocNm;

    @Column(name = "opar_nm", length = 150)
    private String oparNm;

    @Column(name = "game_begin_de")
    private LocalDateTime gameBeginDe;  // datetime

    @Column(name = "game_end_de")
    private LocalDateTime gameEndDe;    // datetime

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "intrl_game_at")
    private Boolean internationalGameAt;  // 1=국제, 0=국내

    @Column(name = "game_type", length = 10)
    private String gameType;

    @Column(name = "partcpt_scale_value", length = 50)
    private String partcptScaleValue;

    @Column(name = "partcpt_scale_cat", length = 20)
    private String partcptScaleCategory;

    @Column(name = "game_aprvl_nm", length = 100)
    private String gameApprovalNm;

    @Column(name = "game_flag_nm", length = 50)
    private String gameFlagNm;

    @Column(name = "hmpg_url", length = 300)
    private String homepageUrl;

    @Column(name = "item_code")
    private Integer itemCode;
}
