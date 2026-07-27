package com.examportal.service;



import com.examportal.dtos.AdminLoginRequest;

public interface AdminService {

	String login(AdminLoginRequest request);

}