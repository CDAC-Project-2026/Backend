package com.examportal.service;

import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.dtos.Registration;
import com.examportal.entities.Student;
import com.examportal.repository.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
	
	private final StudentRepository studentrepo;
	
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

}
