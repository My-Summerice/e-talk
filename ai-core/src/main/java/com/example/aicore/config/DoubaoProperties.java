package com.example.aicore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author summerice
 * @since 2025-07-16 16:24:25
 */
@Configuration
@ConfigurationProperties(prefix = "spring.ai.doubao")
@Data
public class DoubaoProperties {

    private String apiKey;
    private String baseUrl;
    private String modelText;
    private String modelImage;

    private Chat chat = new Chat();

    @Data
    public static class Chat {
        private Options options = new Options();
    }

    @Data
    public static class Options {
        private String model;
        private Double temperature;
        private Integer maxTokens;
    }
}
