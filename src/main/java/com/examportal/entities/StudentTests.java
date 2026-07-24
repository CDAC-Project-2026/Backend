package com.examportal.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="student_test", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","test_id"})) //which student took which tesy
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentTests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_test_id")
    private Long studentTestId;

    
    //many student-tests --> 1 test
    //from test perspective, 1 test --> * students 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    //many student-tests --> 1 student
    //from student perspective, 1 student--> * tests
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "student_score")
    private BigDecimal studentScore;

    @Column(name = "attempted_date")
    private LocalDateTime attemptedDate;

    //for 1 test, 1 student will have many answers.
    //1 studenttest (a paprticular test attempt)--> * answers
    @OneToMany(mappedBy = "studentTest", cascade = CascadeType.ALL)
    private List<StudentAnswers> answers = new ArrayList<>();
	
}
