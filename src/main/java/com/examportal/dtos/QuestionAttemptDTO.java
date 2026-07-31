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
public class QuestionAttemptDTO {
	private Long questionId;
	private String queDescription;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private BigDecimal marks;
	
}
