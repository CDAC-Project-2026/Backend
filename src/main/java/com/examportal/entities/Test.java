package com.examportal.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name="test")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Test {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long TestId;
	
	@Column(name = "student_score")
    private BigDecimal studentScore;

    @Column(name = "attempted_date")
    private LocalDateTime attemptedDate;
    
    //many tests --> 1 course
    //owning side, manytoone
    @ManyToOne
    @JoinColumn(name="course_id")
    private Courses courses;
    
    //1 test --> * answers, onetomany
    @OneToMany(mappedBy="test")
    private List<Questions> questions = new ArrayList<>();
    
    //1 test --> * studentTests 
    @OneToMany(mappedBy="test")
    private List<StudentTests> studentTests = new ArrayList<>();
}



