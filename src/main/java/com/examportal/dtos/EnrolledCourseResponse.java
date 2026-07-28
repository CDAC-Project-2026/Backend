package com.examportal.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrolledCourseResponse {
	private Long enrollmentId;
	private Long courseId;
	private String courseName;
	private String description;
	private LocalDateTime enrollmentDate;
	private BigDecimal progress;
}
