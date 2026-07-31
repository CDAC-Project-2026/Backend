package com.examportal.dtos;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDetailDTO {
	private String testName;
	private String courseName;
	private BigDecimal totalScore;
	private BigDecimal studentScore;
	private List<QuestionResultDTO> questions;
}
