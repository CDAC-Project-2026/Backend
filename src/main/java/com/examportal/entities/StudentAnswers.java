package com.examportal.entities;

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
@Table(name="student_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswers {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="student_ans_id")
	private Long studentAnsId;
	
	//from studentTest perspective, (1 student + 1 test) -> many Student_answers
	//* student_answers --> 1 studentTest
	@ManyToOne
	@JoinColumn(name = "student_test_id")
	private StudentTests studentTest;
	
	//from question's perspective, 1 question --> * student answers
	//* student answers --> 1 question 
	@ManyToOne
	@JoinColumn(name="ques_id")
	private Questions question;
	
	@Column(name = "answer_option")
	private Integer answerOption;	
	
}
