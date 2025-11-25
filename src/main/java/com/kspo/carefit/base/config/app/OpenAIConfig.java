package com.kspo.carefit.base.config.app;

import com.theokanning.openai.service.OpenAiService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Getter
public class OpenAIConfig {

    @Value("${spring.openai.api-key}")
    private String apiKey;

    @Value("${spring.openai.model}")
    private String model;

    @Value("${spring.openai.timeout}")
    private Long timeout;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(apiKey, Duration.ofMillis(timeout));
    }
}
