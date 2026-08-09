package com.examportal.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.StudentActivityLog;

public interface StudentActivityLogRepository extends JpaRepository<StudentActivityLog, Long>{
	
	List<StudentActivityLog> findAllByOrderByLogTimeDesc(Pageable pageable);
}	
