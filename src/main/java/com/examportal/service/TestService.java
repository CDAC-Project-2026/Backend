package com.examportal.service;

import java.util.List;

import com.examportal.dtos.CourseResultDTO;
import com.examportal.dtos.CreateTestDTO;
import com.examportal.dtos.TestListDTO;
import com.examportal.entities.Questions;
import com.examportal.entities.Test;

public interface TestService {
	String createNewTest(Long courseId, Test test, List<Questions> questions);

	List<TestListDTO> getTestsByCourse(Long courseId);

	String publishTest(Long testId);

	String editTest(Long testId, Test testEditted, List<Questions> questionsEditted);

	String deleteTest(Long testId);

	CreateTestDTO getTestById(Long testId);

}
