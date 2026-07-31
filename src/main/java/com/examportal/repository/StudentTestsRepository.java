package com.examportal.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.examportal.dtos.AttemptedTestDTO;
import com.examportal.dtos.StudentResultDTO;
import com.examportal.entities.StudentTests;

public interface StudentTestsRepository extends JpaRepository<StudentTests, Long>{
	
	boolean existsByStudentStudentIdAndTestTestId(Long studentId, Long testId);
	
	@Query("""
		    SELECT new com.examportal.dtos.AttemptedTestDTO(
		        st.test.testName, st.attemptedDate, (st.studentScore * 100 / st.test.totalScore))
		    FROM StudentTests st
		    WHERE st.student.studentId = :studentId
		    ORDER BY st.attemptedDate DESC
		    """)
		List<AttemptedTestDTO> findAttemptedTestsByStudent(@Param("studentId") Long studentId);
	
	
	@Query("""
	        SELECT st FROM StudentTests st
	        WHERE st.student.studentId = :studentId AND st.test.testId = :testId
	        """)
	    Optional<StudentTests> findByStudentIdAndTestId(@Param("studentId") Long studentId, @Param("testId") Long testId);
	
	
	@Query("""
	        SELECT st.studentScore FROM StudentTests st
	        WHERE st.student.studentId = :studentId
	        ORDER BY st.attemptedDate DESC
	        """)
	    List<BigDecimal> findRecentScores(@Param("studentId") Long studentId, Pageable pageable);
	
	
	
	// coursewise results fetch
	@Query("""
	        SELECT COALESCE(AVG(st.studentScore), 0) FROM StudentTests st
	        WHERE st.test.courses.courseId = :courseId
	        """)
	BigDecimal findAverageScoreByCourse(@Param("courseId") Long courseId);

	@Query("""
	        SELECT COALESCE(MAX(st.studentScore), 0) FROM StudentTests st
	        WHERE st.test.courses.courseId = :courseId
	        """)
	BigDecimal findHighestScoreByCourse(@Param("courseId") Long courseId);

	@Query("""
	        SELECT COALESCE(MIN(st.studentScore), 0) FROM StudentTests st
	        WHERE st.test.courses.courseId = :courseId
	        """)
	BigDecimal findLowestScoreByCourse(@Param("courseId") Long courseId);

	@Query("""
	        SELECT new com.examportal.dtos.StudentResultDTO(
	            s.studentId, s.name, st.studentScore, null, 0)
	        FROM StudentTests st
	        JOIN st.student s
	        WHERE st.test.courses.courseId = :courseId
	        ORDER BY st.studentScore DESC
	        """)
	List<StudentResultDTO> findStudentResultsByCourse(@Param("courseId") Long courseId);

	@Query("""
	        SELECT COUNT(st) FROM StudentTests st
	        WHERE st.student.studentId = :studentId
	          AND st.test.courses.courseId = :courseId
	        """)
	Long countAttemptedTestsByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
