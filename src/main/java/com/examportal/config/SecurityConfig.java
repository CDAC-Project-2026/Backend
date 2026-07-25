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

	// Security configuration for application endpoints
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
	    // JWT based APIs don't use CSRF protection
	    .csrf(csrf -> csrf.disable())

	    // Don't create HTTP sessions Every request must carry a JWT
	    .sessionManagement(session ->
	            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

	    // Public and protected endpoints.
	    .authorizeHttpRequests(auth -> auth

	    	    // Public APIs
	    	    .requestMatchers(
	    	            "/student/register",
	    	            "/student/login",
	    	            "/swagger-ui/**",
	    	            "/v3/api-docs/**"
	    	    ).permitAll()

	    	    // Everything else requires JWT
	    	    .anyRequest().authenticated()
	    	)

	    // Execute our JWT filter before Spring's authentication filter
	    .addFilterBefore(
	            jwtAuthenticationFilter,
	            UsernamePasswordAuthenticationFilter.class)

	    .httpBasic(Customizer.withDefaults());

		return http.build();
	}

}