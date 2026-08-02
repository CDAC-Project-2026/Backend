package com.examportal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.BadRequestException;
import com.examportal.custom_exceptions.ForbiddenException;
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
import com.examportal.entities.Student;
import com.examportal.entities.StudentAnswers;
import com.examportal.entities.StudentTests;
import com.examportal.entities.Test;
import com.examportal.repository.StudentEnrolledCoursesRepository;
import com.examportal.repository.StudentRepository;
import com.examportal.repository.StudentTestsRepository;
import com.examportal.repository.TestRepository;
import com.examportal.utils.AuthUtil;
import com.examportal.repository.QuestionsRepository;
import com.examportal.repository.StudentAnswersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentTestsServiceImpl implements StudentTestsService{
	
	private final TestRepository testRepo;
	
	private final StudentTestsRepository studentTestsRepo;
	
	private final StudentEnrolledCoursesRepository studentEnrolledCoursesRepo;
	
	private final StudentRepository studentRepo;
	
	private final StudentAnswersRepository studentAnswersRepo;
	
	private final QuestionsRepository questionsRepo;
	
	private final AuthUtil authUtil;

	@Override
	public List<StudentTestListDTO> getTestsCoursewise() {
		
		Long studentId = authUtil.getCurrentStudentId();
		
		List<StudentTestListDTO> testList = testRepo.findAvailableTestsForStudent(studentId);
		
		return testList;
		
	}

	@Override
	public TestAttemptDTO startTest(Long testId) {
		
		Long studentId = authUtil.getCurrentStudentId();
		
		Test test = testRepo.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test Not Found"));
		
		if(test.getDraft() == true) {
			throw new BadRequestException("This test is not available yet for students");
		}
		
		if(test.getDueDateTime().isBefore(LocalDateTime.now())) {
			throw new BadRequestException("This test cannot be attempted after the due date is passed");
		}
		
		if(studentEnrolledCoursesRepo.existsByStudentStudentIdAndCourseCourseId(studentId, test.getCourses().getCourseId()) == false) {
			throw new ForbiddenException("Student is not enrolled to this course");
		}
		
		if(studentTestsRepo.existsByStudentStudentIdAndTestTestId(studentId, testId) == true) {
			throw new BadRequestException("Student has already attempted this test");
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
	public String submitTest(SubmitTestDTO request) {
		
		Student student = authUtil.getCurrentStudent();
		
		System.out.println("testId=" + request.getTestId() + ", studentId=" + student.getStudentId());
		
		Test test = testRepo.findById(request.getTestId())
	            .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

	    // create the StudentTests record first
	    StudentTests studentTest = new StudentTests();
	    studentTest.setStudent(student);
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
	        
	        //safety check
	        if(ans.getSelectedOption() > 4 || ans.getSelectedOption() < 1) {
	        	ans.setSelectedOption(null);
	        	continue;
	        }
	        
	        Questions question = questionMap.get(ans.getQuestionId());
	        if (question == null) continue; // safety check, ignore invalid question IDs

	        StudentAnswers answerEntity = new StudentAnswers();
	        answerEntity.setStudentTest(studentTest);
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
	public List<AttemptedTestDTO> getResultList() {
		
		Long studentId = authUtil.getCurrentStudentId();
		
		List<AttemptedTestDTO> resultList = studentTestsRepo.findAttemptedTestsByStudent(studentId);
		return resultList;
	}

	@Override
	public TestResultDetailDTO getTestResult(Long testId) {
		
		Long studentId = authUtil.getCurrentStudentId();
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
	
	
	@Override
	public List<StudentTestListDTO> getUnattemptedTestsForCourse(Long courseId) {
	    Long studentId = authUtil.getCurrentStudentId();
	    return testRepo.findAvailableTestsForStudentAndCourse(studentId, courseId);
	}

}
