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
import com.backend.backend.repositories.UserRepository;

@Configuration
public class SpringSecurityConfig {
  
  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Autowired
  private UserRepository userRepository;

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
    
      //USERS ENDPOINTS
      .requestMatchers(HttpMethod.GET, "/users/checkToken").permitAll()
      .requestMatchers(HttpMethod.GET, "/users","/users/{id}", "/users/page/{page}").hasRole("ADMIN")
      .requestMatchers(HttpMethod.POST, "/users","/users/account").permitAll()      
      .requestMatchers(HttpMethod.PUT, "/users","/users/{id}").hasAnyRole("USER","ADMIN")
      .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAnyRole("ADMIN")
      //POSTS ENDPOINTS
      .requestMatchers(HttpMethod.GET, "/posts", "/posts/{id}", "/posts/page/{page}", "/posts/page/{page}/{query}").permitAll()
      .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole("USER","ADMIN")
      .requestMatchers(HttpMethod.PUT, "/posts","/posts/{id}").hasAnyRole("USER","ADMIN")
      .requestMatchers(HttpMethod.DELETE, "/posts").hasAnyRole("USER","ADMIN")
      //COMMENTS ENDPOINTS
      .requestMatchers(HttpMethod.GET, "/comments", "/comments/page/{page}/{id}").permitAll()
      .requestMatchers(HttpMethod.POST, "/comments").hasAnyRole("USER","ADMIN")
      //AUTH ENDPOINTS
      .requestMatchers(HttpMethod.POST, "/auth/checktoken").permitAll()
      .requestMatchers(HttpMethod.POST, "/auth").permitAll()

      .anyRequest().authenticated())
      .csrf(config -> config.disable())
      .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), userRepository))
      .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
      .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource(){
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
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
