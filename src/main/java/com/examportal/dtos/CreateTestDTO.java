package com.examportal.dtos;



import java.util.List;

import com.examportal.entities.Questions;
import com.examportal.entities.Test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTestDTO {
	private Test test;
	private List<Questions> questions;
}
