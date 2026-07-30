package com.examportal.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrolledStudentResponse {
	private Long studentId;
	private String name;
	private String email;
	private BigDecimal progress;
}
