package com.kspo.carefit.base.config.app;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@ConditionalOnExpression("!'${spring.gemini.api-key:}'.isEmpty() && '${spring.openai.api-key:}'.isEmpty()")
public class GeminiConfig {

    @Value("${spring.gemini.api-key}")
    private String apiKey;

    @Value("${spring.gemini.model}")
    private String model;

    @Value("${spring.gemini.api-url}")
    private String apiUrl;

    @Value("${spring.gemini.timeout}")
    private Long timeout;
}
