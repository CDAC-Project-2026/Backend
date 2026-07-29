package com.examportal.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    System.out.println("URL = " + request.getRequestURI());

	    final String authHeader = request.getHeader("Authorization");
	    System.out.println("Authorization = " + authHeader);

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        System.out.println("No Bearer token");
	        filterChain.doFilter(request, response);
	        return;
	    }

	    final String jwt = authHeader.substring(7);
	    System.out.println("JWT = " + jwt);

	    final String email = jwtService.extractUsername(jwt);
	    System.out.println("Email = " + email);
		
		

		// Authenticate only if the user is not already authenticated.
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) 
		{

		    // Load the student details from the database using the email.
		    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

		    // Verify that the token belongs to this user and has not expired.
		    if (jwtService.isTokenValid(jwt, userDetails)) 
		    {

		        // Create an Authentication object after successful JWT validation.
		        UsernamePasswordAuthenticationToken authentication =
		                new UsernamePasswordAuthenticationToken(
		                        userDetails, //yk what userDetails is
		                        null, // here comes the password but yk we already verify the user
		                        userDetails.getAuthorities()); // ikde roles yenar

		        // Store the authenticated user for this request.
		        SecurityContextHolder.getContext().setAuthentication(authentication);
		    }
		}

		filterChain.doFilter(request, response);

	}
}