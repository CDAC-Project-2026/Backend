package com.examportal.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttemptedTestDTO {
	private Long testId;
	private String testName;
	private LocalDateTime attempDateTime;
	private BigDecimal scorePercentage;
}
