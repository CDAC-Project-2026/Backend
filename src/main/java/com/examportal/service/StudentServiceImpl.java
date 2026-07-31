package com.examportal.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.NotificationDTO;
import com.examportal.dtos.Registration;
import com.examportal.dtos.StudentDashboardDTO;
import com.examportal.dtos.StudentTestListDTO;
import com.examportal.entities.Student;
import com.examportal.repository.NotificationRepository;
import com.examportal.repository.StudentRepository;
import com.examportal.repository.StudentTestsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
	
	private final StudentRepository studentrepo;
	
	@Autowired
	StudentTestsRepository studentTestsRepo;
	
	@Autowired
	NotificationRepository notificationRepo;
	
	@Override
	public String registerStudent(Registration request) {
		if(studentrepo.existsByEmail(request.getEmail())) {
			throw new ResourceAlreadyExistsException("Already Registered");
		}
		
		Student s = new Student();
		s.setName(request.getName());
		s.setEmail(request.getEmail());
		s.setPassword(request.getPassword());
		s.setCity(request.getCity());
		s.setPhone(request.getPhone());
		
		studentrepo.save(s);
		
		return "Registration Successfull";
	}

	@Override
	public StudentDashboardDTO getDashboard(Long studentId) {
		Student student = studentrepo.findById(studentId)
	            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

	    List<BigDecimal> recentScores = studentTestsRepo.findRecentScores(studentId, PageRequest.of(0, 4));
	    List<NotificationDTO> notifications = notificationRepo.findNotificationsForStudent(studentId);

	    return new StudentDashboardDTO(
	            student.getName(),
	            student.getStudentRank(),
	            recentScores,
	            notifications
	    );
	}


}
