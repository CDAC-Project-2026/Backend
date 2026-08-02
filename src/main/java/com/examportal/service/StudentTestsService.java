package com.examportal.service;

import java.util.List;

import com.examportal.dtos.AttemptedTestDTO;
import com.examportal.dtos.StudentTestListDTO;
import com.examportal.dtos.SubmitTestDTO;
import com.examportal.dtos.TestAttemptDTO;
import com.examportal.dtos.TestResultDetailDTO;

public interface StudentTestsService {

	List<StudentTestListDTO> getTestsCoursewise();

	TestAttemptDTO startTest(Long testId);

	String submitTest(SubmitTestDTO submitTest);
	
	List<AttemptedTestDTO> getResultList();

	TestResultDetailDTO getTestResult(Long testId);
	
	List<StudentTestListDTO> getUnattemptedTestsForCourse(Long courseId);
	
}
