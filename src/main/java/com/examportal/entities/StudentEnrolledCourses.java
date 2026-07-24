package com.examportal.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_enrollment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrolledCourses {

	// Primary key for Student Enrollment table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enrollment_id")
	private Long enrollmentId;

	// Stores the date when the student enrolled in the course
	@Column(name = "enrollment_date")
	private LocalDateTime enrollmentDate;

	// One student can have multiple course enrollments
	// The foreign key is stored in this table
	@ManyToOne //* Courses --> one Student
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	// One course can have multiple enrolled students
	// The foreign key is stored in this table
	@ManyToOne //* enrolled students --> Student
	@JoinColumn(name = "course_id", nullable = false)
	private Courses course;

	// Tracks the student's course completion percentage
	private BigDecimal progress;

}