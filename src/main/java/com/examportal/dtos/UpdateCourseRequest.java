package com.examportal.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseRequest {
	@NotBlank
    private String courseName;
    private String description;
}
