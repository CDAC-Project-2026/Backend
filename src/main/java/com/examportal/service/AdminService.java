package com.examportal.service;



import java.util.List;

import com.examportal.dtos.AdminProfileResponse;
import com.examportal.dtos.ChangePasswordRequest;
import com.examportal.dtos.CreateUserRequest;
import com.examportal.dtos.UpdateAdminProfileRequest;
import com.examportal.dtos.UserResponse;
import com.examportal.dtos.AdminDashboardDTO;

public interface AdminService {

	AdminProfileResponse getProfile();

	String updateProfile(UpdateAdminProfileRequest request);

	String changePassword(ChangePasswordRequest request);
	
	List<UserResponse> getAllUsers();
	
	String createUser(CreateUserRequest request);
	
	String updateUser(CreateUserRequest request);

	String deleteUser(String email);
  
	AdminDashboardDTO getDashboardData();

}
