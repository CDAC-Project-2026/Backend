package com.examportal.dtos;

import java.util.List;

import com.examportal.entities.StudentActivityLog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDTO {
	private Long noOfStudents;
	private Long noOfCourses;
	private Long noOfTests;
	private Double averageScore;
	private List<StudentActivityLog> studentLogs;
}
