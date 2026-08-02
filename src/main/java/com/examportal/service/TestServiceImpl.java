package com.examportal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.BadRequestException;
import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.CreateTestDTO;
import com.examportal.dtos.TestListDTO;
import com.examportal.entities.Courses;
import com.examportal.entities.Notification;
import com.examportal.entities.Questions;
import com.examportal.entities.Test;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.NotificationRepository;
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

	private final NotificationRepository notificationRepo;
	
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
		
		Notification notification = new Notification();
		notification.setCourse(test.getCourses());
		notification.setDescription("A new test \"" + test.getTestName() + "\" has been published.");
		notification.setNotifTime(LocalDateTime.now());
		notificationRepo.save(notification);
		
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
	
	
	@Override

	public CreateTestDTO getTestById(Long testId) {
		Test existingTest = testRepo.findById(testId)
				.orElseThrow(() -> new ResourceNotFoundException("Test Not Found"));


		Test testCopy = new Test();
		testCopy.setTestId(existingTest.getTestId());
		testCopy.setTestName(existingTest.getTestName());
		testCopy.setTotalScore(existingTest.getTotalScore());
		testCopy.setScheduleTime(existingTest.getScheduleTime());
		testCopy.setDueDateTime(existingTest.getDueDateTime());
		testCopy.setTimeAlloted(existingTest.getTimeAlloted());
		testCopy.setDraft(existingTest.getDraft());



		List<Questions> questionsCopy = existingTest.getQuestions().stream()
				.map(q -> {
					Questions qc = new Questions();
					qc.setQueId(q.getQueId());
					qc.setQueDescription(q.getQueDescription());
					qc.setOptionA(q.getOptionA());
					qc.setOptionB(q.getOptionB());
					qc.setOptionC(q.getOptionC());
					qc.setOptionD(q.getOptionD());
					qc.setCorrectAnswer(q.getCorrectAnswer());
					qc.setMarks(q.getMarks());
					return qc;
				})
				.collect(Collectors.toList());

		CreateTestDTO dto = new CreateTestDTO();

		dto.setTest(testCopy);
		dto.setQuestions(questionsCopy);

		return dto;
	}

}
