package com.examportal.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

	// Primary key for Student table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Long studentId;

	// Basic student details
	private String name;

	@Column(unique = true)
	private String email;

	private String password;

	private String phone;

	private String city;

	@Column(name = "rank")
	private Integer studentRank;

	// One student can enroll in multiple courses
	// The foreign key is maintained in StudentEnrolledCourses
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<StudentEnrolledCourses> enrolledCourses = new ArrayList<>();

	// One student can attempt multiple tests
	// StudentTests is the owning side because it stores student_id
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<StudentTests> studentTests = new ArrayList<>();

}