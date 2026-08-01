package com.examportal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.BadRequestException;
import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.TestListDTO;
import com.examportal.entities.Courses;
import com.examportal.entities.Questions;
import com.examportal.entities.Test;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.QuestionsRepository;
import com.examportal.repository.TestRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TestServiceImpl implements TestService{
	
	private final TestRepository testRepo;
	
	private final QuestionsRepository questionRepo;
	
	private final CourseRepository courseRepo;

	
	@Override 
	public String createNewTest(Long courseId, Test test, List<Questions> questions) {

		Courses courses = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));
		
		test.setCourses(courses);
		test.setDraft(true);
		Test savedTest = testRepo.save(test);
		
		questions.forEach(q -> q.setTest(savedTest));
		questionRepo.saveAll(questions);
		
		return "Test drafted successfully";
		
	}
	
	@Override 
	public List<TestListDTO> getTestsByCourse(Long courseId){
		
		if (!courseRepo.existsById(courseId)) {
	        throw new ResourceNotFoundException("Course Not Found");
	    }
		
		List<TestListDTO> testList = testRepo.findTestSummariesByCourse(courseId);
		
		return testList;
	}

	@Override
	public String publishTest(Long testId) {
		Test test = testRepo.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test Not Found"));
		
		test.setDraft(false);
		//due to automatic dirty checking, even if we dont save this test, hibernate compares the state of object with when it was loaded
		//and automatically performs UPDATE on the db.
		testRepo.save(test);
		
		return "Test is published to the students.";
	}

	@Override
	public String editTest(Long testId, Test testEditted, List<Questions> questionsEditted) {
		Test existingTest = testRepo.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test Not Found"));
		
		if(existingTest.getDraft() == false) {
			throw new BadRequestException("Cannot edit published test");
		}
		
		existingTest.setTotalScore(testEditted.getTotalScore());
		existingTest.setScheduleTime(testEditted.getScheduleTime());
		existingTest.setTimeAlloted(testEditted.getTimeAlloted());
		existingTest.setDueDateTime(testEditted.getDueDateTime());
		
		existingTest.getQuestions().clear();
		
		questionsEditted.forEach(q -> q.setTest(existingTest));
		existingTest.getQuestions().addAll(questionsEditted);
		
		testRepo.save(existingTest);
		
		return "Test updated successfully";
		
	}
	

	@Override
	public String deleteTest(Long testId) {
		Test test = testRepo.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test Not Found"));
		
		if(test.getDraft() == false) {
			throw new BadRequestException("Published test cannot be deleted.");
		}
		
		if(!test.getStudentTests().isEmpty()) {
			throw new BadRequestException("Tests which students have attempted cannot be deleted.");
		}
		
		testRepo.delete(test);
		return "Test Deleted Successfully";
	}

}
