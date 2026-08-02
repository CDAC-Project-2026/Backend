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
public class StudentDashboardDTO {
	private String studentName;
	private Integer studentRank;
	private List<BigDecimal> testScores;
	private List<NotificationDTO> notifications;
}
