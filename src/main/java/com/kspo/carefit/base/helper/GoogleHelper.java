package com.kspo.carefit.base.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class GoogleHelper {

    public static HttpHeaders googleHeaders(String key,
                                            String fieldMask){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key",key);
        headers.set("X-Goog-FieldMask", fieldMask);
        return headers;

    }
}
