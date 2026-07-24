package com.examportal.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testId;

    @Column(name = "total_score")
    private BigDecimal totalScore;

    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime;

    @Column(name = "due_time")
    private LocalDateTime dueTime;

    @Column(name = "time_alloted")
    private Integer timeAlloted;

    // Many Tests -> One Course
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;

    // One Test -> Many Questions
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Questions> questions = new ArrayList<>();

    // One Test -> Many Student Tests
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<StudentTests> studentTests = new ArrayList<>();
}