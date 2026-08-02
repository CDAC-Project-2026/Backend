package com.examportal.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	private Long notifId;
    private String description;
    private LocalDateTime notifTime;
    private String courseName; 
}
