package com.examportal.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {
	private Long courseId;
	private String courseName;
	private String description;
	private LocalDateTime createdAt;

	private String adminName;
}