package com.examportal.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {

    private String subject;
    private String question;
}