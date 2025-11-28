package com.kspo.carefit.damain.carpool.dto.response;

import java.time.Instant;
import java.util.List;

public record MyCarpoolResponse (List<MyPost> postList){

    public record MyPost(Long id,
                         String title,
                         Instant postedAt){

    }
}
