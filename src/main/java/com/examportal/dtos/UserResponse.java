package com.examportal.dtos;

import com.examportal.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Role role;
}