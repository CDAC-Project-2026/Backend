package com.examportal.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.CourseResultDTO;
import com.examportal.dtos.StudentResultDTO;
import com.examportal.entities.Courses;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.StudentTestsRepository;
import com.examportal.repository.TestRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ResultsServiceImpl implements ResultsService{
	
	private final StudentTestsRepository studentTestsRepo;
	
	private final CourseRepository courseRepo;
	
	private final TestRepository testRepo;
	
	private String calculateGrade(BigDecimal score) {
	    double s = score.doubleValue();
	    if (s >= 90) return "A+";
	    if (s >= 80) return "A";
	    if (s >= 70) return "B";
	    if (s >= 60) return "C";
	    return "F";
	}

	
	@Override
	public CourseResultDTO getCoursewiseResults(Long courseId) {
		Courses course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));

	    BigDecimal avgScore = studentTestsRepo.findAverageScoreByCourse(courseId);
	    BigDecimal highScore = studentTestsRepo.findHighestScoreByCourse(courseId);
	    BigDecimal lowScore = studentTestsRepo.findLowestScoreByCourse(courseId);

	    List<StudentResultDTO> results = studentTestsRepo.findStudentResultsByCourse(courseId);

	    long totalTestsInCourse = testRepo.countByCoursesCourseId(courseId);

	    for (StudentResultDTO r : results) {
	        r.setGrade(calculateGrade(r.getStudentScore()));

	        long attemptedCount = studentTestsRepo.countAttemptedTestsByStudentAndCourse(r.getStudentId(), courseId);
	        int progress = totalTestsInCourse == 0 ? 0 : (int) ((attemptedCount * 100) / totalTestsInCourse);
	        r.setProgress(progress);
	    }

	    return new CourseResultDTO(course.getCourseId(), course.getCourseName(), avgScore, highScore, lowScore, results);
	}
}
