package com.examportal.service;

import com.examportal.dtos.LoginRequest;
import com.examportal.dtos.LoginResponse;

public interface AuthenticationService {

	LoginResponse login(LoginRequest request);

}