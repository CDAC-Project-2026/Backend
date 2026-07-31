package com.examportal.entities;

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
@Table(name="courses")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Courses {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="course_id")
	private Long courseId;
	
	@Column(name = "course_name")
    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    //many courses created by --> 1 admin,  many to one, owning
    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin admin;
    
    //1 course can have --> * enrollments, onetomany, inverse side
    @OneToMany(mappedBy="course")
    private List<StudentEnrolledCourses> studentsEnrolled = new ArrayList<>();
    
    //1 course --> * study materials
    @OneToMany(mappedBy = "course")
    private List<StudyMaterial> studyMaterials = new ArrayList<>();
    
    //1 course can have --> * tests
    @OneToMany(mappedBy = "courses")
    private List<Test> tests = new ArrayList<>();
    
    //1 course can have --> * notifications
    @OneToMany(mappedBy = "course")
    private List<Notification> notifications = new ArrayList<>();
}
