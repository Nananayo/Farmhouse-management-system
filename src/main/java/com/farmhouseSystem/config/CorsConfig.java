package com.farmhouseSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    // 当前跨域请求最大有效时长。这里默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 1 设置访问源地址
        corsConfiguration.setAllowedHeaders(Arrays.asList("*")); // 2 设置访问源请求头
        corsConfiguration.setAllowedMethods(Arrays.asList("*")); // 3 设置访问源请求方法
        corsConfiguration.setMaxAge(MAX_AGE);
        corsConfiguration.setAllowCredentials(true); // 允许发送凭证
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
