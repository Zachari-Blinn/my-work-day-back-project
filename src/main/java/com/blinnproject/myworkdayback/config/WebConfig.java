package com.blinnproject.myworkdayback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/api/**")
      .allowedOrigins("http://localhost:4200")
      .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
      .allowedHeaders("*")
      .exposedHeaders("*")
      .allowCredentials(true).maxAge(3600);

    // Add more mappings...
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration
        .defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(60))
        .serializeValuesWith(RedisSerializationContext
            .SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }

//  @Bean
//  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//    return (builder) -> builder
//        .withCacheConfiguration("userCache",
//            RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(20)))
//        .withCacheConfiguration("dataCache",
//            RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(5)));
//  }
}