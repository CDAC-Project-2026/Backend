package com.examportal.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Questions {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "que_id")
	 private Long queId;

	 @Column(name = "que_description", columnDefinition = "TEXT")
	 private String queDescription;

	 private String optionA;

	 private String optionB;

	 private String optionC;
	 
	 private String optionD;

	 @Column(name = "correct_answer")
	 private Integer correctAnswer;

	 private BigDecimal marks;
	 
	 //many questions --> 1 test , many to one, owning side
	 @ManyToOne
	 @JoinColumn(name="test_id")
	 private Test test;
	 
	 //1 question --> many student Answers, one to many
	 @OneToMany(mappedBy = "question")
	 private List<StudentAnswers> studentsAnswers = new ArrayList<>(); 	 
}
