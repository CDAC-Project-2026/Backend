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
@Table(name="student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="student_id")
	private Long studentId;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private String phone;
	
	private String city;
	
	@Column(name="student_rank")
	private Integer studentRank;
	
	//1 student can be enrolled in multiple courses, hence they can have a list of enrollment details
	//1 student --> * enrollments
	@OneToMany(mappedBy ="student",cascade = CascadeType.ALL)
	private List<StudentEnrolledCourses> enrolledCourses = new ArrayList<>();
	
	//1 student can have many test. 
	//1 student --> * tests
	//one to many
	@OneToMany(mappedBy="", cascade = CascadeType.ALL)
	private List<StudentTests> tests = new ArrayList<>();

}
