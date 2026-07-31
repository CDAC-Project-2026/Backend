package com.examportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.examportal.dtos.TestListDTO;
import com.examportal.entities.Questions;
import com.examportal.entities.Test;
import com.examportal.service.ResultsService;
import com.examportal.service.TestService;



@RestController
public class TestController {

	@Autowired
	private TestService service;
	
	@Autowired 
	private ResultsService resultService;
	
	@PostMapping("/admin/course/{courseId}/test/new")
	public ResponseEntity<?> createTest(@PathVariable Long courseId, @RequestBody CreateTestDTO createTestDTO) {
	    try {
	        String messageString = service.createNewTest(courseId, createTestDTO.getTest(), createTestDTO.getQuestions());
	        return ResponseEntity.status(HttpStatus.CREATED).body(messageString);
	    }
	    catch(ResourceNotFoundException ex) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
	    catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to create test: " + e.getMessage());
	    }
	}
	
	
	@GetMapping("/admin/{courseId}/tests")
	public ResponseEntity<?> getTestsByCourse(@PathVariable Long courseId){
		try {
			List<TestListDTO> testList = service.getTestsByCourse(courseId);
			return ResponseEntity.status(HttpStatus.OK).body(testList);
		} 
		catch(ResourceNotFoundException ex) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
	    catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to get tests by course id: " + e.getMessage());
	    }
	}
	
	
	
	@PatchMapping("/admin/test/{testId}/publish")
	public ResponseEntity<?> publishTest(@PathVariable Long testId){
		try {
			
			String message = service.publishTest(testId);
			return ResponseEntity.status(HttpStatus.OK).body(message);
			
		} 
		catch(ResourceNotFoundException ex) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
	    catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to publish test: " + e.getMessage());
	    }
	}
	
	
	@PutMapping("/admin/test/{testId}/edit")
	public ResponseEntity<?> editTest(@PathVariable Long testId, @RequestBody CreateTestDTO createTestDTO){
		try {
			Test test = createTestDTO.getTest();
			List<Questions> questions = createTestDTO.getQuestions();
			String message = service.editTest(testId, test, questions);
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} 
		catch(ResourceNotFoundException ex) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
	    catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to edit test: " + e.getMessage());
	    }
	}
	
	
	@DeleteMapping("/admin/test/{testId}/delete")
	public ResponseEntity<?> deleteTest(@PathVariable Long testId){
		try {
			String message = service.deleteTest(testId);
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} 
		catch(ResourceNotFoundException ex) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
	    catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to delete test: " + e.getMessage());
	    }
	}
	
	
	// view course wise results 
	@GetMapping("/admin/course/{courseId}/results")
	public ResponseEntity<?> getCoursewiseResults(@PathVariable Long courseId){
		try {
			CourseResultDTO courseResult = resultService.getCoursewiseResults(courseId);
			
			return ResponseEntity.status(HttpStatus.OK).body(courseResult);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Results Not Found");
			
		}
	}
	
	
	
	
	
	
}
