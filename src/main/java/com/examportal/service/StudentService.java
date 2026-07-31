package com.examportal.service;


import java.util.List;

import com.examportal.dtos.Registration;
import com.examportal.dtos.StudentDashboardDTO;

public interface StudentService {

	String registerStudent(Registration request);

	StudentDashboardDTO getDashboard(Long studentId);

}
