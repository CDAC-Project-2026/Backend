package com.examportal.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Study_material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long doc_id;
	
	private String doc_title;
	
	@Column(columnDefinition = "TEXT")
	private String doc_description;
	
	@Column(name="doc_size")
	private BigDecimal docSize;
	
	@Column(name="doc_url")
	private String docUrl;
	
	//many study-material --> 1 course, many to one, owning side
	@ManyToOne
	@JoinColumn(name="course_id")
	private Courses course;
	
}
