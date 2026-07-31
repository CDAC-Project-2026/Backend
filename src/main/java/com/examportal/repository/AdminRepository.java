package com.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{

}
