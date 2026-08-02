package com.examportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.CourseResultDTO;
import com.examportal.dtos.CreateTestDTO;
import com.examportal.dtos.ResponseDTO;
import com.examportal.dtos.TestListDTO;
import com.examportal.entities.Questions;
import com.examportal.entities.Test;
import com.examportal.service.ResultsService;
import com.examportal.service.TestService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class TestController {

	private final TestService testService;
	
	private final ResultsService resultService;
	
	@PostMapping("/admin/course/{courseId}/test/new")
	public ResponseEntity<ResponseDTO<String>> createTest(@PathVariable Long courseId, @RequestBody CreateTestDTO createTestDTO) {
	    
	    return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    testService.createNewTest(courseId, createTestDTO.getTest(), createTestDTO.getQuestions())
	            )
	);
	}
	
	
	@GetMapping("/admin/{courseId}/tests")
	public ResponseEntity<ResponseDTO<List<TestListDTO>>> getTestsByCourse(@PathVariable Long courseId){
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    testService.getTestsByCourse(courseId)
	            )
	);
	}
	
	
	
	@PatchMapping("/admin/test/{testId}/publish")
	public ResponseEntity<ResponseDTO<String>> publishTest(@PathVariable Long testId){
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    testService.publishTest(testId)
	            )
	);
	}
	
	
	@PutMapping("/admin/test/{testId}/edit")
	public ResponseEntity<ResponseDTO<String>> editTest(@PathVariable Long testId, @RequestBody CreateTestDTO createTestDTO){
		
		Test test = createTestDTO.getTest();
		List<Questions> questions = createTestDTO.getQuestions();
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    testService.editTest(testId, test, questions)
	            )
	);
		
	}
	
	
	@DeleteMapping("/admin/test/{testId}/delete")
	public ResponseEntity<ResponseDTO<String>> deleteTest(@PathVariable Long testId){
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    testService.deleteTest(testId)
	            )
	);
	}
	
	
	@GetMapping("/admin/test/{testId}")
	public ResponseEntity<ResponseDTO<CreateTestDTO>> getTestById(@PathVariable Long testId){

		return ResponseEntity.ok(

	            new ResponseDTO<>(
	                    "Success",
	                    testService.getTestById(testId)
	            )
	);
	}


	
	// view course wise results 
	@GetMapping("/admin/course/{courseId}/results")
	public ResponseEntity<ResponseDTO<CourseResultDTO>> getCoursewiseResults(@PathVariable Long courseId){		
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    resultService.getCoursewiseResults(courseId)
	            )
	);
	}
	
	
	
	
	
	
}
