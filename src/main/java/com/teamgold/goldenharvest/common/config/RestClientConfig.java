package com.teamgold.goldenharvest.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient RestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://www.kamis.or.kr")
                .defaultHeader(HttpHeaders.USER_AGENT,
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .defaultHeader(HttpHeaders.CONNECTION, "close")
                .build();
    }
}
