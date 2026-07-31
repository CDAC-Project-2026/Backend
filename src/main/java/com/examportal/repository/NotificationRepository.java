package com.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.examportal.dtos.NotificationDTO;
import com.examportal.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	@Query("""
	        SELECT new com.examportal.dtos.NotificationDTO(
	            n.notifId, n.description, n.notifTime, n.course.courseName)
	        FROM Notification n
	        WHERE n.course.courseId IN (
	            SELECT sec.course.courseId FROM StudentEnrolledCourses sec
	            WHERE sec.student.studentId = :studentId
	        )
	        ORDER BY n.notifTime DESC
	        """)
	    List<NotificationDTO> findNotificationsForStudent(@Param("studentId") Long studentId);
}
