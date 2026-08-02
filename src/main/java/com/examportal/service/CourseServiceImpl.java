package com.examportal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.CourseResponse;
import com.examportal.dtos.CreateCourseRequest;
import com.examportal.dtos.UpdateCourseRequest;
import com.examportal.entities.Admin;
import com.examportal.entities.Courses;
import com.examportal.repository.AdminRepository;
import com.examportal.repository.CourseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
	
	private final CourseRepository courserepo;
	private final AdminRepository adminrepo;
	
	//helper method
	private Admin getCurrentAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		
		return adminrepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
	}

	@Override
	public String createCourse(CreateCourseRequest request) {
		Admin admin = getCurrentAdmin();
		Courses c = new Courses();
		c.setCourseName(request.getCourseName());
		c.setDescription(request.getDescription());
		c.setCreatedAt(LocalDateTime.now());
		c.setAdmin(admin);
		
		courserepo.save(c);
		
		return "Course created successfully";
	}

	@Override
	public String updateCourse(Long courseId, UpdateCourseRequest request) {
		Courses course = courserepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course not found"));
		
		course.setCourseName(request.getCourseName());
		course.setDescription(request.getDescription());
		
		courserepo.save(course);
		
		return "Course updated successfully";
	}

	@Override
	public String deleteCourse(Long courseId) {
		Courses c = courserepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course not found"));
		courserepo.delete(c);
		
		if (!c.getStudentsEnrolled().isEmpty()
				|| !c.getTests().isEmpty()
				|| !c.getStudyMaterials().isEmpty()
				|| !c.getNotifications().isEmpty()) {
			throw new ResourceAlreadyExistsException(
					"Cannot delete this course — it still has enrolled students, tests, "
					+ "study materials, or notifications linked to it. Remove those first.");
		}
		
		return "Course deleted successfully";
	}

	@Override
	public CourseResponse getCourseById(Long courseId) {
		Courses c = courserepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course not found"));
		CourseResponse res = new CourseResponse();
		res.setCourseId(c.getCourseId());
		res.setCourseName(c.getCourseName());
		res.setDescription(c.getDescription());
		res.setCreatedAt(c.getCreatedAt());
		res.setAdminName(c.getAdmin() != null ? c.getAdmin().getName() : null);
		
		return res; 	
	}

	@Override
	public List<CourseResponse> getAllCourses() {
		return courserepo.findAll()
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}
	
	//helper 
	private CourseResponse mapToResponse(Courses course) {
		CourseResponse response = new CourseResponse();

		response.setCourseId(course.getCourseId());
		response.setCourseName(course.getCourseName());
		response.setDescription(course.getDescription());
		response.setCreatedAt(course.getCreatedAt());
		response.setAdminName(
				course.getAdmin() != null ? course.getAdmin().getName() : null);

		return response;
	}

}
