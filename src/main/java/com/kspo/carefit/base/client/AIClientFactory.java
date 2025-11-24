package com.kspo.carefit.base.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIClientFactory {

    private final ApplicationContext applicationContext;

    @Value("${spring.ai.provider}")
    private String provider;

    /**
     * 설정된 AI 제공자에 따라 적절한 AIClient를 반환합니다.
     *
     * @return AIClient 구현체
     */
    public AIClient getAIClient() {
        AIClient client = switch (provider.toLowerCase()) {
            case "openai" -> applicationContext.getBean("openAIClient", AIClient.class);
            case "gemini" -> applicationContext.getBean("geminiClient", AIClient.class);
            default -> {
                log.warn("지원하지 않는 AI 제공자: {}. OpenAI를 사용합니다.", provider);
                yield applicationContext.getBean("openAIClient", AIClient.class);
            }
        };

        log.info("사용 중인 AI 제공자: {}", client.getProviderName());
        return client;
    }
}
