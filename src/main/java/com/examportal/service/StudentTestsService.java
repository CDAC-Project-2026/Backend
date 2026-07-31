package com.examportal.service;

import java.util.List;

import com.examportal.dtos.AttemptedTestDTO;
import com.examportal.dtos.StudentTestListDTO;
import com.examportal.dtos.SubmitTestDTO;
import com.examportal.dtos.TestAttemptDTO;
import com.examportal.dtos.TestResultDetailDTO;

public interface StudentTestsService {

	List<StudentTestListDTO> getTestsCoursewise(Long studentId);

	TestAttemptDTO startTest(Long studentId, Long testId);

	String submitTest(SubmitTestDTO submitTest);
	
	List<AttemptedTestDTO> getResultList(Long studentId);

	TestResultDetailDTO getTestResult(Long studentId, Long testId);
	
}
