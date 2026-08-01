package com.examportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.dtos.AdminProfileResponse;
import com.examportal.dtos.ChangePasswordRequest;
import com.examportal.dtos.CreateUserRequest;
import com.examportal.dtos.LoginRequest;
import com.examportal.dtos.LoginResponse;
import com.examportal.dtos.ResponseDTO;
import com.examportal.dtos.UpdateAdminProfileRequest;
import com.examportal.dtos.UserResponse;
import com.examportal.dtos.AdminDashboardDTO;

import com.examportal.service.AdminService;
import com.examportal.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;
    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        "Success",
                        authenticationService.login(request)));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO<AdminProfileResponse>> getProfile() {

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        "Success",
                        adminService.getProfile()));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<ResponseDTO<String>> updateProfile(
            @Valid @RequestBody UpdateAdminProfileRequest request) {

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        "Success",
                        adminService.updateProfile(request)));
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<ResponseDTO<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        "Success",
                        adminService.changePassword(request)));
    }
    
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO<List<UserResponse>>> getAllUsers() {

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        "Success",
                        adminService.getAllUsers()));
    }
    
    @PostMapping("/users")
    public ResponseEntity<ResponseDTO<String>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(
                        "Success",
                        adminService.createUser(request)));
    }
    
    @PutMapping("/users")
    public ResponseEntity<ResponseDTO<String>> updateUser(
            @Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        "Success",
                        adminService.updateUser(request)));
    }
    
    @DeleteMapping("/users/{email}")
    public ResponseEntity<ResponseDTO<String>> deleteUser(
            @PathVariable String email) {

        return ResponseEntity.ok(
                new ResponseDTO<>(
                        "Success",
                        adminService.deleteUser(email)));
    }
  
    @GetMapping("/dashboard")
    public ResponseEntity<ResponseDTO<AdminDashboardDTO>> getAdminDashboard(){
  
	    return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    adminService.getDashboardData()));
      
    }
}
