package com.examportal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.dtos.AttemptedTestDTO;
import com.examportal.dtos.ResponseDTO;
import com.examportal.dtos.StudentTestListDTO;
import com.examportal.dtos.SubmitTestDTO;
import com.examportal.dtos.TestAttemptDTO;
import com.examportal.dtos.TestResultDetailDTO;
import com.examportal.service.StudentTestsService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentTestsController {
	
	private final StudentTestsService studentTestsService;
	
	@GetMapping("/tests/all")
	public ResponseEntity<ResponseDTO<List<StudentTestListDTO>>> getUnattemptedTests(){
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    studentTestsService.getTestsCoursewise()
	            )
	);
		
	}
	
	@GetMapping("/test/{testId}")
	public ResponseEntity<ResponseDTO<TestAttemptDTO>> startTest(@PathVariable Long testId){
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    studentTestsService.startTest(testId)
	            )
	);
	}
	
	@PostMapping("/submit")
	public ResponseEntity<ResponseDTO<String>> submitTest(@RequestBody SubmitTestDTO submitTest){
//		System.out.println("CONTROLLER: testId=" + submitTest.getTestId() + ", studentId=" + submitTest.getStudentId());
		System.out.println("DTO class loaded from: " + submitTest.getClass().getProtectionDomain().getCodeSource().getLocation());
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    studentTestsService.submitTest(submitTest)
	            )
	);
		
	}
	
	
	// get all attempted tests
	@GetMapping("/result")
	public ResponseEntity<ResponseDTO<List<AttemptedTestDTO>>> getResultsList(){
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    studentTestsService.getResultList()
	            )
	);
	}
	
	//get result for a particular attempted test
	@GetMapping("/test/{testId}/result")
	public ResponseEntity<ResponseDTO<TestResultDetailDTO>> getTestResult(@PathVariable Long testId){
		
		return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    studentTestsService.getTestResult(testId)
	            )
	);
	}
	
	
	@GetMapping("/courses/{courseId}/tests")
	public ResponseEntity<ResponseDTO<List<StudentTestListDTO>>> getUnattemptedTestsForCourse(
	        @PathVariable Long courseId) {
	    return ResponseEntity.ok(
	            new ResponseDTO<>(
	            		"Success", 
	            		studentTestsService.getUnattemptedTestsForCourse(courseId)
	            )
	    );
	}
}
