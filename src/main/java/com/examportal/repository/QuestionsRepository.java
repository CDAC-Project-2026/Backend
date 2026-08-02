package com.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.examportal.dtos.QuestionResultDTO;
import com.examportal.entities.Questions;

public interface QuestionsRepository extends JpaRepository<Questions, Long>{
	@Query("""
	        SELECT new com.examportal.dtos.QuestionResultDTO(
	            q.queId, q.queDescription, q.optionA, q.optionB, q.optionC, q.optionD,
	            q.correctAnswer, sa.answerOption)
	        FROM Questions q
	        LEFT JOIN StudentAnswers sa
	            ON sa.question.queId = q.queId AND sa.studentTest.studentTestId = :studentTestId
	        WHERE q.test.testId = :testId
	        """)
	    List<QuestionResultDTO> findQuestionResultsByTestAndStudentTest(
	            @Param("testId") Long testId, @Param("studentTestId") Long studentTestId);
}	
