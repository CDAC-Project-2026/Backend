package com.examportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
	private String message;
	private T data;
	
	public ResponseDTO(String message) {
		super();
		this.message = message;
		this.data = null;
	}
}
