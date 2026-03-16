package com.github.nikita65288.config;

import com.github.nikita65288.exception.GlobalExceptionHandler;
import com.github.nikita65288.jwt.JwtProperties;
import com.github.nikita65288.jwt.JwtProvider;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(GlobalExceptionHandler.class)
@EnableConfigurationProperties(JwtProperties.class)
public class AutoConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * The bean will only be created if the service has specified jwt.secret in application.yml
     */
    @Bean
    @ConditionalOnProperty(name = "jwt.secret")
    public JwtProvider jwtProvider(JwtProperties properties) {

        if (properties.getExpiration() != null) {
            // For services that generate tokens
            return new JwtProvider(properties.getSecret(), properties.getExpiration());
        } else {
            // For services that only verify tokens
            return new JwtProvider(properties.getSecret());
        }
    }
}
