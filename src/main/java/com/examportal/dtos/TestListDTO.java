package com.examportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestListDTO {
	private Long testId;
	private String testName;
	private Integer duration;
	private Long noOfQuestions;
	private Boolean draft;
}
