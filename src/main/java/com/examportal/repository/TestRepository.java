package com.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.examportal.dtos.StudentTestListDTO;
import com.examportal.dtos.TestListDTO;
import com.examportal.entities.Courses;
import com.examportal.entities.Test;

public interface TestRepository extends JpaRepository<Test, Long>{

	List<Test> findByCourses(Courses courses);
	
	@Query("""
		    SELECT new com.examportal.dtos.TestListDTO(
        t.testId, t.testName, t.timeAlloted, COUNT(q), t.draft)
    FROM Test t
    LEFT JOIN t.questions q
    WHERE t.courses.courseId = :courseId
    GROUP BY t.testId, t.testName, t.timeAlloted
		    """)
		List<TestListDTO> findTestSummariesByCourse(@Param("courseId") Long courseId);
	
	
	@Query("""
		    SELECT new com.examportal.dtos.StudentTestListDTO(
		        t.testId, t.testName, COUNT(q), t.timeAlloted, t.totalScore, t.courses.courseName)
		    FROM Test t
		    LEFT JOIN t.questions q
		    WHERE t.draft = false
		      AND t.dueDateTime > CURRENT_TIMESTAMP
		      AND EXISTS (
		          SELECT 1 FROM StudentEnrolledCourses sec
		          WHERE sec.student.studentId = :studentId
		            AND sec.course.courseId = t.courses.courseId
		      )
		      AND NOT EXISTS (
		          SELECT 1 FROM StudentTests st
		          WHERE st.student.studentId = :studentId
		            AND st.test.testId = t.testId
		      )
		    GROUP BY t.testId, t.testName, t.timeAlloted, t.totalScore, t.courses.courseName
		    """)
		List<StudentTestListDTO> findAvailableTestsForStudent(@Param("studentId") Long studentId);
	
	Long countByCoursesCourseId(Long courseId);
	
	Long countByCoursesCourseIdAndDraftFalse(Long courseId);
	
	
	@Query("""
	        SELECT new com.examportal.dtos.StudentTestListDTO(
	            t.testId, t.testName, COUNT(q), t.timeAlloted, t.totalScore, t.courses.courseName)
	        FROM Test t
	        LEFT JOIN t.questions q
	        WHERE t.draft = false
	          AND t.dueDateTime > CURRENT_TIMESTAMP
	          AND t.courses.courseId = :courseId
	          AND EXISTS (
	              SELECT 1 FROM StudentEnrolledCourses sec
	              WHERE sec.student.studentId = :studentId
	                AND sec.course.courseId = t.courses.courseId
	          )
	          AND NOT EXISTS (
	              SELECT 1 FROM StudentTests st
	              WHERE st.student.studentId = :studentId
	                AND st.test.testId = t.testId
	          )
	        GROUP BY t.testId, t.testName, t.timeAlloted, t.totalScore, t.courses.courseName
	        """)
	List<StudentTestListDTO> findAvailableTestsForStudentAndCourse(
	        @Param("studentId") Long studentId, @Param("courseId") Long courseId);

}
