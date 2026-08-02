package com.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.StudentAnswers;

public interface StudentAnswersRepository extends JpaRepository<StudentAnswers, Long>{

}
