package com.examportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.dtos.AdminDashboardDTO;
import com.examportal.service.AdminService;


@RestController
public class AdminController {
	
	@Autowired
	private AdminService service;
	
	@GetMapping("/admin/dashboard")
	public ResponseEntity<?> getAdminDashboard(){
		try {
			AdminDashboardDTO dashboard = service.getDashboardData();
			return ResponseEntity.status(HttpStatus.OK).body(dashboard);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin Dashboard Data Not Found");
		}
	}
}
