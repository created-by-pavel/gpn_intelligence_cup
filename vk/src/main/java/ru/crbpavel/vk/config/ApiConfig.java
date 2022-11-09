package ru.crbpavel.vk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "vk")
public class ApiConfig {
    private String token;
    private Integer clientId;
}
