package com.examportal.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
    private Long testId;
	
	@Column(name = "test_name")
	private String testName;
	
	@Column(name = "total_score")
    private BigDecimal totalScore;

    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime;
    
    @Column(name = "due_date_time")
    private LocalDateTime dueDateTime;
    
    @Column(name = "time_alloted")
    private Integer timeAlloted;
    
    @Column(name = "draft")
    private Boolean draft;
    
    //many tests --> 1 course
    //owning side, manytoone
    @ManyToOne
    @JoinColumn(name="course_id")
    private Courses courses;
    
    //1 test --> * answers, onetomany
    @OneToMany(mappedBy="test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questions = new ArrayList<>();
    
    //1 test --> * studentTests 
    //mappedBy tells JPA that Test is not the owner of this attribute
    @OneToMany(mappedBy="test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentTests> studentTests = new ArrayList<>();
}



