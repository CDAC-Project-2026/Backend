package com.examportal.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.examportal.dtos.StudyMaterialResponse;

public interface StudyMaterialService {
	List<StudyMaterialResponse> getAllStudyMaterialForCourse(Long courseId);
	
	StudyMaterialResponse uploadStudyMaterial(Long courseId, MultipartFile file, String title, String description);
	
	String deleteMaterial(Long materialId);
	
	List<StudyMaterialResponse> getMaterialsForStudent(Long courseId);
}
