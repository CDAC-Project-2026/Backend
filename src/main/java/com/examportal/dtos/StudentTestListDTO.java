package com.examportal.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentTestListDTO {
	private Long testId;
	private String testName;
	private Long noOfQuestions;
	private Integer timeAlloted;
	private BigDecimal totalScore;
	private String courseName;
}
