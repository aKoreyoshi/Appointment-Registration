package com.mac.ghpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月03日, 21:04:16
 */
@Configuration
public class GatewayCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); // 允许所有来源
        corsConfig.addAllowedMethod("*"); // 允许所有HTTP方法
        corsConfig.addAllowedHeader("*"); // 允许所有请求头


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);

    }
}