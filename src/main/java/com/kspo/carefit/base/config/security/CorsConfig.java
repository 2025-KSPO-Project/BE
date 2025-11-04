package com.kspo.carefit.base.config.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource configurationSource(){

        return new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();

                // 허용할 Origin 설정 (프론트엔드 주소)
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                // 허용할 HTTP 메서드 설정 (모든 메서드 허용)
                configuration.setAllowedMethods(Collections.singletonList("*"));
                // 자격 증명(쿠키 등) 포함 요청 허용 여부
                configuration.setAllowCredentials(true);
                // 허용할 HTTP 헤더 설정 (모든 헤더 허용)
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                // Preflight 요청의 결과를 캐시할 시간 (초 단위)
                configuration.setMaxAge(3600L);

                // 브라우저에 노출할 헤더 설정 (Authorization 헤더를 클라이언트가 읽을 수 있게 함)
                configuration.addExposedHeader("Authorization");
                // 쿠키 관련 헤더가 필요하다면 추가
                // configuration.addExposedHeader("Set-Cookie");

                return configuration;
            }
        };
    }
}
