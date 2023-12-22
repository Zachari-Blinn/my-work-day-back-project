package com.blinnproject.myworkdayback.security;

import com.blinnproject.myworkdayback.security.jwt.AuthTokenFilter;
import com.blinnproject.myworkdayback.security.jwt.JwtAuthenticationEntryPoint;
import com.blinnproject.myworkdayback.security.services.UserDetailsServiceImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity(
    securedEnabled = true,
    prePostEnabled = true,
    jsr250Enabled = true
)
public class WebSecurityConfig {

  private static final AntPathRequestMatcher[] SWAGGER_REQUEST_WHITE_LIST = {
      antMatcher("/swagger-ui.html"),
      antMatcher("/v3/api-docs/**"),
      antMatcher("/swagger-ui/**"),
      antMatcher("/v2/api-docs/**"),
      antMatcher("/swagger-resources/**")
  };

  private static final AntPathRequestMatcher[] API_REQUEST_WHITE_LIST = {
      antMatcher("/api/auth/**"),
      antMatcher("/api/training/**"),
      antMatcher("/api/exercise/**")
  };

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private JwtAuthenticationEntryPoint authenticationEntryPoint;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
            )
        );
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .requestMatchers(SWAGGER_REQUEST_WHITE_LIST).permitAll()
            .requestMatchers(API_REQUEST_WHITE_LIST).permitAll()
            .anyRequest().authenticated()
        )
      .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
        .bearerFormat("JWT")
        .scheme("bearer");
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().addSecurityItem(new SecurityRequirement().
            addList("Bearer Authentication"))
        .components(new Components().addSecuritySchemes
            ("Bearer Authentication", createAPIKeyScheme()))
        .info(new Info().title("My REST API")
        .description("Some custom description of API.")
        .version("1.0")
        .license(new License().name("License of API")
        .url("API license URL")));
  }
}