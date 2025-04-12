package com.lfpsys.lfpsys_tax_calculation_services.redis;

import static java.util.Objects.nonNull;
import static org.springframework.data.redis.connection.RedisPassword.of;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private Integer redisPort;

  @Value("${spring.data.redis.password}")
  private String redisPassword;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    if (nonNull(redisHost) && nonNull(redisPort) && nonNull(redisPassword)) {
      RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
      redisConfig.setHostName(redisHost);
      redisConfig.setPort(redisPort);
      redisConfig.setPassword(of(redisPassword));
      return new LettuceConnectionFactory(redisConfig);
    }
    return new LettuceConnectionFactory();
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory());
    return template;
  }
}
