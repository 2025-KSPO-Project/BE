package com.kspo.carefit.damain.apply.entity;

import com.kspo.carefit.damain.carpool.entity.Carpool;
import com.kspo.carefit.damain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


import java.time.Instant;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "carpool_id")
    private Carpool carpool;

    private Instant applyAt;

}
