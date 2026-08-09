package com.examportal.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSource {

    private String file;
    private String page;

    public ChatSource(String file, String page) {
        this.file = file;
        this.page = page;
    }
}