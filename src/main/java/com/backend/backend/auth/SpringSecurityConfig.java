package com.backend.backend.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.backend.backend.auth.filters.JwtAuthenticationFilter;
import com.backend.backend.auth.filters.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {
  
  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager() throws Exception{
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    return http.authorizeHttpRequests(authRules -> authRules
      .requestMatchers(HttpMethod.GET, "/users").permitAll()
      .requestMatchers(HttpMethod.GET, "/users/{id}").permitAll()
      .requestMatchers(HttpMethod.GET, "/posts").permitAll()
      .requestMatchers(HttpMethod.GET, "/comments").permitAll()
      .requestMatchers(HttpMethod.POST, "/users").hasAnyRole("ADMIN")
      .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole("USER","ADMIN")
      .requestMatchers(HttpMethod.PUT, "/posts").hasAnyRole("USER","ADMIN")
      .requestMatchers(HttpMethod.POST, "/comments").hasAnyRole("USER","ADMIN")
      .anyRequest().authenticated())
      .csrf(config -> config.disable())
      .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
      .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
      .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource(){
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    config.setAllowedOriginPatterns(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  FilterRegistrationBean<CorsFilter> corsFilter(){
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
      new CorsFilter(corsConfigurationSource()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }














}
