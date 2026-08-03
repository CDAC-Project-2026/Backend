package com.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.StudyMaterial;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long>{
	List<StudyMaterial> findByCourse_CourseId(Long courseId);
}
