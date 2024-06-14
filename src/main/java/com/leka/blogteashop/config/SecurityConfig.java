package com.leka.blogteashop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "/create-post",
                                "/addPost",
                                "/edit-post/**",
                                "/editPost/**"
                        ).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(PathRequest.toStaticResources()
                                .atCommonLocations()).permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
