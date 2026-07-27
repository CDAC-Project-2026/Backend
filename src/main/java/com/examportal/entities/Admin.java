package com.examportal.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.examportal.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    private String password;
    
    private String email;
    
    private String phone;
    
	 // Role assigned to the admin.
	 // Stored as text in the database.
	 @Enumerated(EnumType.STRING)
	 private Role role;

    //1 admin creates --> * courses, one to many, inverse side
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Courses> courses = new ArrayList<>();
}