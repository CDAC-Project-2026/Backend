package com.examportal.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.StudyMaterialResponse;
import com.examportal.entities.Courses;
import com.examportal.entities.Student;
import com.examportal.entities.StudyMaterial;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.EnrollmentRepository;
import com.examportal.repository.StudentRepository;
import com.examportal.repository.StudyMaterialRepository;

import lombok.RequiredArgsConstructor;



@Service
@Transactional
@RequiredArgsConstructor
public class StudyMaterialServiceImpl implements StudyMaterialService {
	
	private final StudyMaterialRepository studyMaterialRepo;
	private final CourseRepository courseRepo;
	private final StudentRepository studentRepo;
	private final EnrollmentRepository enrollmentRepo;
	
	private static final Path UPLOAD_DIR = Paths.get("uploads", "study-materials");
	
	@Override
	public List<StudyMaterialResponse> getAllStudyMaterialForCourse(Long courseId) {

		if (!courseRepo.existsById(courseId)) {
			throw new ResourceNotFoundException("Course not found");
		}

		return studyMaterialRepo.findByCourse_CourseId(courseId)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public StudyMaterialResponse uploadStudyMaterial(Long courseId, MultipartFile file, String title, String description) {
		Courses c = courseRepo.findById(courseId).orElseThrow(()-> new ResourceNotFoundException("Course Not Found"));
		
		if(file==null || file.isEmpty()) {
			throw new IllegalArgumentException("No file was uploaded");
		}
		
		try {
			Files.createDirectories(UPLOAD_DIR);
			
			String filename = file.getName();
			String extension = (filename != null && filename.contains(".")) ? filename.substring(filename.lastIndexOf('.')): "";

			String storedFilename = UUID.randomUUID() + extension;
			file.transferTo(UPLOAD_DIR.resolve(storedFilename));
			
			StudyMaterial material = new StudyMaterial();
			material.setDocTitle((title != null && !title.isBlank()) ? title : filename);
			material.setDocDescription(description);
			material.setDocSize(BigDecimal.valueOf(file.getSize()).divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP));
			material.setDocUrl("/files/study-materials/" + storedFilename);
			material.setCourse(c);

			return mapToResponse(studyMaterialRepo.save(material));
			
		}catch(IOException e) {
			throw new RuntimeException("Could not store the uploaded file.", e);
		}
	}
	

	private StudyMaterialResponse mapToResponse(StudyMaterial material) {
		StudyMaterialResponse response = new StudyMaterialResponse();
		response.setDocId(material.getDocId());
		response.setDocTitle(material.getDocTitle());
		response.setDocDescription(material.getDocDescription());
		response.setDocSize(material.getDocSize());
		response.setDocUrl(material.getDocUrl());
		return response;
	}

	@Override
	public String deleteMaterial(Long materialId) {
		StudyMaterial material = studyMaterialRepo.findById(materialId).orElseThrow(() -> new ResourceNotFoundException("Study material not found"));

		studyMaterialRepo.delete(material);

		try {
			String filename = Paths.get(material.getDocUrl()).getFileName().toString();
			Files.deleteIfExists(UPLOAD_DIR.resolve(filename));
		} catch (IOException ignored) {
			
		}

		return "Study material deleted successfully";
	}
	
	
	@Override
	public List<StudyMaterialResponse> getMaterialsForStudent(Long courseId) {

		Student student = getCurrentStudent();

		boolean enrolled = enrollmentRepo.existsByStudent_StudentIdAndCourse_CourseId(
				student.getStudentId(), courseId);

		if (!enrolled) {
			throw new ResourceNotFoundException("Course not found");
		}

		return studyMaterialRepo.findByCourse_CourseId(courseId)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	private Student getCurrentStudent() {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();

		return studentRepo.findByEmail(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));
	}

}
