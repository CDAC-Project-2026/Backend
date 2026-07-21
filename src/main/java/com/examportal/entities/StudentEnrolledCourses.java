package com.examportal.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Student_Enrollment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentEnrolledCourses {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long enrollment_id;
	
	private LocalDateTime enrollmentDate;
	
	//multiple enrollments, (in multiple courses) by 1 student, manyToOne , owning side 
	//Enrollment * --> 1 student
	@ManyToOne
	@JoinColumn(name="student_id", nullable=false)
	private Student student;
	
	//many enrollments in 1 course, manyToOne, owning side 
	//Enrollmetn * --> 1 course
	@ManyToOne
	@JoinColumn(name="course_id", nullable=false)
	private Courses course;
	
	private BigDecimal progress;
}

