package com.examportal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examportal.dtos.AdminDashboardDTO;
import com.examportal.repository.AdminRepository;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.StudentRepository;
import com.examportal.repository.TestRepository;

@Service
public class AdminServiceImpl implements AdminService{

    private final StudentServiceImpl studentServiceImpl;
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired 
	private StudentRepository studentRepo;
	
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private TestRepository testRepo;

    AdminServiceImpl(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

	@Override
	public AdminDashboardDTO getDashboardData() {

		
		long noOfStudents = studentRepo.count();
		long noOfCourses = courseRepo.count();
		long noOfTests = testRepo.count();
		
		System.out.println("noof student : " + noOfStudents + " no of courses : " + noOfCourses + " no of tests : " + noOfTests);
		// average score
		Double averageScore = 0.0;
		
		// logs
		List<String> studentLogs = new ArrayList<>(List.of("Student logged in", "Student attempted a test"));
			
		return new AdminDashboardDTO(noOfStudents, noOfCourses, noOfTests, averageScore, studentLogs);
	}
	
	
}	
