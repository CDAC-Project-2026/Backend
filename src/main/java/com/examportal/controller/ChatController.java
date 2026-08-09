package com.examportal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examportal.dtos.ChatRequest;
import com.examportal.dtos.ChatResponse;
import com.examportal.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/student/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> ask(
            @RequestBody ChatRequest request) {

        return ResponseEntity.ok(
                chatService.askQuestion(request)
        );
    }
}