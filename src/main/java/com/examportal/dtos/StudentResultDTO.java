package com.examportal.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResultDTO {
	private Long studentId;
	private String studentName;
	private BigDecimal studentScore;
	private String grade;
	private Integer progress;
}
