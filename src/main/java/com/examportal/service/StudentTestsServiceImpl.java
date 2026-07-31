package com.examportal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.AnswerDTO;
import com.examportal.dtos.AttemptedTestDTO;
import com.examportal.dtos.QuestionAttemptDTO;
import com.examportal.dtos.QuestionResultDTO;
import com.examportal.dtos.StudentTestListDTO;
import com.examportal.dtos.SubmitTestDTO;
import com.examportal.dtos.TestAttemptDTO;
import com.examportal.dtos.TestResultDetailDTO;
import com.examportal.entities.Questions;
import com.examportal.entities.StudentAnswers;
import com.examportal.entities.StudentTests;
import com.examportal.entities.Test;
import com.examportal.repository.StudentEnrolledCoursesRepository;
import com.examportal.repository.StudentRepository;
import com.examportal.repository.StudentTestsRepository;
import com.examportal.repository.TestRepository;
import com.examportal.repository.QuestionsRepository;
import com.examportal.repository.StudentAnswersRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentTestsServiceImpl implements StudentTestsService{
	
	@Autowired
	TestRepository testRepo;
	
	@Autowired 
	StudentTestsRepository studentTestsRepo;
	
	@Autowired
	StudentEnrolledCoursesRepository studentEnrolledCoursesRepo;
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	StudentAnswersRepository studentAnswersRepo;
	
	@Autowired
	QuestionsRepository questionsRepo;

	@Override
	public List<StudentTestListDTO> getTestsCoursewise(Long studentId) {
		
		List<StudentTestListDTO> testList = testRepo.findAvailableTestsForStudent(studentId);
		
		return testList;
		
	}

	@Override
	@Transactional
	public TestAttemptDTO startTest(Long studentId, Long testId) {
		
		Test test = testRepo.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test Not Found"));
		
		if(test.getDraft() == true) {
			throw new RuntimeException("This test is not available yet for students");
		}
		
		if(test.getDueDateTime().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("This test cannot be attempted after the due date is passed");
		}
		
		if(studentEnrolledCoursesRepo.existsByStudentStudentIdAndCourseCourseId(studentId, test.getCourses().getCourseId()) == false) {
			throw new RuntimeException("Student is not enrolled to this course");
		}
		
		if(studentTestsRepo.existsByStudentStudentIdAndTestTestId(studentId, testId) == true) {
			throw new RuntimeException("Student has already attempted this test");
		}

		
		List<QuestionAttemptDTO> questionDtos = test.getQuestions().stream()
	            .map(q -> new QuestionAttemptDTO(
	                    q.getQueId(), q.getQueDescription(),
	                    q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD(),
	                    q.getMarks()))
	            .collect(Collectors.toList());
		
		return new TestAttemptDTO(test.getTestId(), test.getTestName(), test.getTimeAlloted(), test.getCourses().getCourseName(), questionDtos);
		
	}

	@Override
	@Transactional
	public String submitTest(SubmitTestDTO request) {
		
		System.out.println("testId=" + request.getTestId() + ", studentId=" + request.getStudentId());
		
		Test test = testRepo.findById(request.getTestId())
	            .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

	    // create the StudentTests record first
	    StudentTests studentTest = new StudentTests();
	    studentTest.setStudent(studentRepo.findById(request.getStudentId())
	            .orElseThrow(() -> new ResourceNotFoundException("Student not found")));
	    studentTest.setTest(test);
	    studentTest.setAttemptedDate(LocalDateTime.now());

	    BigDecimal totalScore = BigDecimal.ZERO;
	    List<StudentAnswers> answerEntities = new ArrayList<>();

	    Map<Long, Questions> questionMap = test.getQuestions().stream()
	            .collect(Collectors.toMap(Questions::getQueId, q -> q));

	    for (AnswerDTO ans : request.getAnswers()) {
	        if (ans.getSelectedOption() == null) {
	            continue; // skipped question — no answer row, no marks, no crash
	        }
	        Questions question = questionMap.get(ans.getQuestionId());
	        if (question == null) continue; // safety check, ignore invalid question IDs

	        StudentAnswers answerEntity = new StudentAnswers();
	        answerEntity.setStudenttest(studentTest);
	        answerEntity.setQuestion(question);
	        answerEntity.setAnswerOption(ans.getSelectedOption());
	        answerEntities.add(answerEntity);

	        if (ans.getSelectedOption().equals(question.getCorrectAnswer())) {
	            totalScore = totalScore.add(question.getMarks());
	        }
	    }

	    studentTest.setStudentScore(totalScore);
	    studentTestsRepo.save(studentTest);      // saves StudentTests row
	    studentAnswersRepo.saveAll(answerEntities); // saves all answer rows

	    return "Test submitted successfully. Score: " + totalScore;
	}

	
	
	@Override
	@Transactional
	public List<AttemptedTestDTO> getResultList(Long studentId) {
		if (!studentRepo.existsById(studentId)) {
	        throw new ResourceNotFoundException("Student not found");
	    }
		
		List<AttemptedTestDTO> resultList = studentTestsRepo.findAttemptedTestsByStudent(studentId);
		return resultList;
	}

	@Override
	@Transactional
	public TestResultDetailDTO getTestResult(Long studentId, Long testId) {
		StudentTests studentTest = studentTestsRepo.findByStudentIdAndTestId(studentId, testId)
	            .orElseThrow(() -> new ResourceNotFoundException("This student has not attempted this test"));

	    Test test = studentTest.getTest();

	    List<QuestionResultDTO> questions = questionsRepo.findQuestionResultsByTestAndStudentTest(
	            testId, studentTest.getStudentTestId());

	    return new TestResultDetailDTO(
	            test.getTestName(),
	            test.getCourses().getCourseName(),
	            test.getTotalScore(),
	            studentTest.getStudentScore(),
	            questions
	    );
		
	}

}
