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
public class SubmitTestDTO {
	private Long testId;
	private Long studentId;
	private List<AnswerDTO> answers;
}
