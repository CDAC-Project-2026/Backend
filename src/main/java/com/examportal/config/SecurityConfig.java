package com.examportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.examportal.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	// Custom filter that validates JWT on every request
	private final  JwtAuthenticationFilter jwtAuthenticationFilter ;

	// Password encoder used to hash passwords before storing them
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// Provides Spring Security's AuthenticationManager bean.
	@Bean
	public AuthenticationManager authenticationManager(
	        AuthenticationConfiguration configuration) throws Exception {

	    return configuration.getAuthenticationManager();
	}

	// Security configuration for application endpoints
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
	    .cors(cors -> cors.configurationSource(corsConfigurationSource()))

	    // JWT based APIs don't use CSRF protection
	    .csrf(csrf -> csrf.disable())

	    // Don't create HTTP sessions Every request must carry a JWT
	    .sessionManagement(session ->
	            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

	    .authorizeHttpRequests(auth -> auth

	    	    // Public endpoints
	    	    .requestMatchers(
	    	            "/student/register",
	    	            "/student/login",
	    	            "/admin/login",
	    	            "/swagger-ui/**",
	    	            "/v3/api-docs/**"
	    	    ).permitAll()

	    	    // Student APIs
	    	    .requestMatchers("/student/**")
	    	    .hasRole("STUDENT")

	    	    // Admin APIs
	    	    .requestMatchers("/admin/**")
	    	    .hasRole("ADMIN")

	    	    // Any other endpoint must be authenticated.
	    	    .anyRequest().authenticated()
	    	)
	    
	    // Execute our JWT filter before Spring's authentication filter
	    .addFilterBefore(
	            jwtAuthenticationFilter,
	            UsernamePasswordAuthenticationFilter.class);


		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

	    CorsConfiguration configuration = new CorsConfiguration();

	    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(List.of("*"));
	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);

	    return source;
	}

}