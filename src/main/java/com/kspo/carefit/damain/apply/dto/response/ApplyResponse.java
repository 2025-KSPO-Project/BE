package com.kspo.carefit.damain.apply.dto.response;

import java.time.Instant;

public record ApplyResponse(Long postId,
                            String title,
                            Instant meetAt) {

}
