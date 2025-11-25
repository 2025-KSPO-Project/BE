package com.kspo.carefit.damain.carpool.dto.response;

import java.time.Instant;

public record CarpoolPostResponse (String title,
                                   String username,
                                   Instant postedAt){
}
