package com.kspo.carefit.base.client;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.security.oauth2.dto.response.NaverRevokeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2Client {

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUri;

    // 소셜 제공 토큰을 파기하는 메소드
    public boolean revokeToken(String token){

        String builder = UriComponentsBuilder
                .fromUriString(tokenUri)
                .queryParam("grant_type","delete")
                .queryParam("access_token",token)
                .queryParam("client_id",clientId)
                .queryParam("client_secret",clientSecret)
                .queryParam("service_provider","NAVER")
                .toUriString();

        try {

            ResponseEntity<NaverRevokeResponse> responseEntity = restTemplate
                    .getForEntity(builder, NaverRevokeResponse.class);

            // 네이버 API가 200 OK 응답을 주었는지 확인
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                NaverRevokeResponse responseBody = responseEntity.getBody();

                // result 필드가 success 인지 확인
                if (responseBody != null &&
                        "success".equals(responseBody.result())) {

                    log.info("Naver token revocation successful: {}", responseBody);
                    return true; // 최종 성공

                } else {

                    log.warn("Naver API returned 2xx but result was not 'success': {}", responseBody);
                    return false; // 네이버 비즈니스 로직상 실패

                }
            } else {
                // 2xx가 아닌 응답 (드물지만 발생 가능)
                log.warn("Naver API returned non-2xx status: {}", responseEntity.getStatusCode());
                return false;

            }

        } catch (HttpClientErrorException e){
            // 토큰 무효화 or 파기
            log.info("Naver API call failed: Token is INVALID or EXPIRED.");
            throw new BaseException(BaseExceptionEnum.FORBIDDEN);

        }
        catch (HttpServerErrorException e) {
            // 서버 오류
            log.error("Naver API error happened by Server");
            throw new BaseException(BaseExceptionEnum.EXCEPTION_ISSUED);

        } catch (Exception e) {
            // 기타 네트워크 오류 등
            log.error("Error calling Naver API: {}", e.getMessage(), e);
            throw new BaseException(BaseExceptionEnum.EXCEPTION_ISSUED);
        }

    }

}
