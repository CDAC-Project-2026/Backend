package com.examportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.examportal.entities.Courses;
import com.examportal.entities.Student;
import com.examportal.entities.StudentEnrolledCourses;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.EnrollmentRepository;
import com.examportal.repository.StudentRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Mock
    private StudentRepository studentrepo;

    @Mock
    private CourseRepository courserepo;

    @Mock
    private EnrollmentRepository enrollmentrepo;
    
    @Test
    void enrollInCourse_Success() {

    	// Mock Spring Security Context
    	Authentication authentication = mock(Authentication.class);
    	SecurityContext securityContext = mock(SecurityContext.class);

    	when(securityContext.getAuthentication()).thenReturn(authentication);
    	when(authentication.getName()).thenReturn("heydven@gmail.com");

    	SecurityContextHolder.setContext(securityContext);
    	
    	// Arrange
        Student student = new Student();
        student.setStudentId(1L);
        student.setEmail("heydven@gmail.com");

        Courses course = new Courses();
        course.setCourseId(1L);

        when(studentrepo.findByEmail(anyString()))
                .thenReturn(java.util.Optional.of(student));

        when(courserepo.findById(1L))
                .thenReturn(java.util.Optional.of(course));

        when(enrollmentrepo.existsByStudent_StudentIdAndCourse_CourseId(1L, 1L))
                .thenReturn(false);

        // Act
        String result = enrollmentService.enrollInCourse(1L);

        // Assert
        assertEquals("Enrolled Successfully", result);

        verify(enrollmentrepo, times(1)).save(any(StudentEnrolledCourses.class));
    }
    
    @Test
    void enrollInCourse_AlreadyEnrolled() {

        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("heydven@gmail.com");

        SecurityContextHolder.setContext(securityContext);

        // Mock Student
        Student student = new Student();
        student.setStudentId(1L);
        student.setEmail("heydven@gmail.com");

        // Mock Course
        Courses course = new Courses();
        course.setCourseId(1L);

        when(studentrepo.findByEmail(anyString()))
                .thenReturn(Optional.of(student));

        when(courserepo.findById(1L))
                .thenReturn(Optional.of(course));

        // Student already enrolled
        when(enrollmentrepo.existsByStudent_StudentIdAndCourse_CourseId(1L,1L))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> enrollmentService.enrollInCourse(1L));

        verify(enrollmentrepo, never()).save(any());
    }

}