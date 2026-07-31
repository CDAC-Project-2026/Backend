package com.examportal.dtos;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResultDTO {
	private Long courseId;
	private String courseName;
	private BigDecimal averageScore;
	private BigDecimal highestScore;
	private BigDecimal lowestScore;
	private List<StudentResultDTO> studentResults; 
}
