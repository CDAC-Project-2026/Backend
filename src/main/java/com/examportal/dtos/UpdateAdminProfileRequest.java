package com.examportal.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAdminProfileRequest {

	@NotBlank
	private String name;

}