package com.examportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

	// Indicates whether the request was successful.
	private String status;

	// Holds the actual response returned to the client.
	private T data;

	public ResponseDTO(String status) {
		this.status = status;
		this.data = null;
	}

}