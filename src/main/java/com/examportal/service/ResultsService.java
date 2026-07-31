package com.examportal.service;

import com.examportal.dtos.CourseResultDTO;

public interface ResultsService {

	CourseResultDTO getCoursewiseResults(Long courseId);

}
