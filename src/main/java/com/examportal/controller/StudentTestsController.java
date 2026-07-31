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
import com.examportal.dtos.StudentTestListDTO;
import com.examportal.dtos.SubmitTestDTO;
import com.examportal.dtos.TestAttemptDTO;
import com.examportal.dtos.TestResultDetailDTO;
import com.examportal.service.StudentTestsService;


@RestController
@RequestMapping("/student")
public class StudentTestsController {
	
	@Autowired 
	StudentTestsService service;
	
	@GetMapping("/{studentId}/tests/all")
	public ResponseEntity<?> getUnattemptedTests(@PathVariable Long studentId){
		try {
			List<StudentTestListDTO> testsList = service.getTestsCoursewise(studentId);
			return ResponseEntity.status(HttpStatus.OK).body(testsList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tests Not Found");
		}
		
	}
	
	@GetMapping("/{studentId}/test/{testId}")
	public ResponseEntity<?> startTest(@PathVariable Long studentId, @PathVariable Long testId){
		try {
			
			System.out.println("loading test.");
			TestAttemptDTO testAttempt = service.startTest(studentId, testId);
			System.out.println("test loaded.");
			
			return ResponseEntity.status(HttpStatus.OK).body(testAttempt);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test Failed to Load");
		}
	}
	
	@PostMapping("/submit")
	public ResponseEntity<?> submitTest(@RequestBody SubmitTestDTO submitTest){
		System.out.println("CONTROLLER: testId=" + submitTest.getTestId() + ", studentId=" + submitTest.getStudentId());
		System.out.println("DTO class loaded from: " + submitTest.getClass().getProtectionDomain().getCodeSource().getLocation());
		try {
			String message = service.submitTest(submitTest);
			
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test could not be submitted");
		}
		
	}
	
	
	// get all attempted tests
	@GetMapping("/{studentId}/result")
	public ResponseEntity<?> getResultsList(@PathVariable Long studentId){
		try {
			
			List<AttemptedTestDTO> resultList= service.getResultList(studentId);
			return ResponseEntity.status(HttpStatus.OK).body(resultList);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attempted Tests Not Found");
		}
	}
	
	//get result for a particular attempted test
	@GetMapping("/{studentId}/test/{testId}/result")
	public ResponseEntity<?> getTestResult(@PathVariable Long studentId, @PathVariable Long testId){
		try {
			TestResultDetailDTO testResultDetailDTO = service.getTestResult(studentId, testId);
			return ResponseEntity.status(HttpStatus.OK).body(testResultDetailDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test result not found");
		}
	}
}
