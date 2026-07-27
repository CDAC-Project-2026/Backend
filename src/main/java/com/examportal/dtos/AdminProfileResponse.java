package com.examportal.dtos;

import com.examportal.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProfileResponse {

    private Long adminId;

    private String email;

    private String name;

    private Role role;
}
