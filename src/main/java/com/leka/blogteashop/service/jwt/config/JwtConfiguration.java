package com.leka.blogteashop.service.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration class that holds jwt properties.
 *
 * @author osynelnyk
 */
@Configuration
@EnableConfigurationProperties(JwtConfiguration.JwtProperties.class)
public class JwtConfiguration {

  /**
   * Jwt properties.
   *
   * @param secretKey key which is used for encoding signature of the jwt token
   */
  @ConfigurationProperties(prefix = "jwt.options")
  public record JwtProperties(String secretKey) {

  }
}
