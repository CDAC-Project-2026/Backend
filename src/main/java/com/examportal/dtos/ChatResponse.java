package com.examportal.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponse {

    private String subject;
    private String question;
    private String answer;
    private List<ChatSource> sources;
}