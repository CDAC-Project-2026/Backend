package com.examportal.entities;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "study_material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyMaterial {

	// Primary key for Study Material table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_id")
	private Long docId;

	// Title displayed to students
	@Column(name = "doc_title")
	private String docTitle;

	// Brief description of the uploaded material
	@Column(name = "doc_description", columnDefinition = "TEXT")
	private String docDescription;

	// Size of the uploaded document (in MB)
	@Column(name = "doc_size")
	private BigDecimal docSize;

	// Location of the uploaded file
	@Column(name = "doc_url")
	private String docUrl;

	// Every study material belongs to a single course.
	// The foreign key is maintained in the StudyMaterial table.
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Courses course;

}