package com.examportal.service;

import org.springframework.stereotype.Service;

import com.examportal.dtos.LoginRequest;
import com.examportal.dtos.LoginResponse;
import com.examportal.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final JwtService jwtService;
	
	private final AuthenticationManager authenticationManager;

	@Override
	public LoginResponse login(LoginRequest request) {

		// Ask Spring Security to authenticate the user.
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()));

		// Authentication successful. Generate JWT.
		String token = jwtService.generateToken(request.getEmail());

		// Return JWT to the client.
		return new LoginResponse(
				token,
				"Bearer",
				"Login successful.");
	}
}