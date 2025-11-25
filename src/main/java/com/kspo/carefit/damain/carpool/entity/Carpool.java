package com.kspo.carefit.damain.carpool.entity;

import com.kspo.carefit.damain.apply.entity.Apply;
import com.kspo.carefit.damain.carpool.vo.Destination;
import com.kspo.carefit.damain.carpool.vo.Spot;
import com.kspo.carefit.damain.carpool.vo.Start;
import com.kspo.carefit.damain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Carpool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Instant postedAt;
    private Instant meetAt;
    private Integer maxCount;

    // 지원자들
    @OneToMany(mappedBy = "carpool",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Apply> applies = new ArrayList<>();

    // 작성자
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "start_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "start_longitude"))
    })
    private Start start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "destination_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "destination_longitude"))
    })
    private Destination destination;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "spot_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "spot_longitude")),
            @AttributeOverride(name = "placeName", column = @Column(name = "spot_place_name")),
            @AttributeOverride(name = "score", column = @Column(name = "spot_score"))
    })
    private Spot spot;

    public void addApply(Apply apply){
        this.applies.add(apply);
        apply.setCarpool(this);
    }

    public void deleteApply(Apply apply){
        this.applies.remove(apply);
        apply.setCarpool(null);
    }



    public Integer getApplyCount(){
        return this.applies.size();
    }



}
