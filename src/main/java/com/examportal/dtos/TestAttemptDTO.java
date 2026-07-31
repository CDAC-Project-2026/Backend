package com.examportal.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestAttemptDTO {
	private Long testId;
	private String testName;
	private Integer timeAlloted;
	private String courseName;
	private List<QuestionAttemptDTO> questions; 
}
