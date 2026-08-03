package com.examportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.examportal.dtos.ResponseDTO;
import com.examportal.dtos.StudyMaterialResponse;
import com.examportal.service.StudyMaterialService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/courses/{courseId}/materials")
@RequiredArgsConstructor
public class StudyMaterialController {

	private final StudyMaterialService studyMaterialService;

	@PostMapping
	public ResponseEntity<ResponseDTO<StudyMaterialResponse>> uploadMaterial(
			@PathVariable Long courseId,
			@RequestParam("file") MultipartFile file,
			@RequestParam("title") String title,
			@RequestParam(value = "description", required = false) String description) {

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("Success", studyMaterialService.uploadStudyMaterial(courseId, file, title, description)));
	}

	@GetMapping
	public ResponseEntity<ResponseDTO<List<StudyMaterialResponse>>> getMaterials(@PathVariable Long courseId) {
		return ResponseEntity.ok(new ResponseDTO<>("Success", studyMaterialService.getAllStudyMaterialForCourse(courseId)));
	}

	@DeleteMapping("/{materialId}")
	public ResponseEntity<ResponseDTO<String>> deleteMaterial(@PathVariable Long courseId,@PathVariable Long materialId) {
		return ResponseEntity.ok(new ResponseDTO<>("Success",studyMaterialService.deleteMaterial(materialId)));
	}
}