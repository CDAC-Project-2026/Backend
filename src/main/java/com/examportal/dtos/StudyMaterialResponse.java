package com.examportal.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyMaterialResponse {
	private Long docId;
	private String docTitle;
	private String docDescription;
	private BigDecimal docSize;
	private String docUrl;
}