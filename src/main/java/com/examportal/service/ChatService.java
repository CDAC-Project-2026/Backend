package com.examportal.service;

import com.examportal.dtos.ChatRequest;
import com.examportal.dtos.ChatResponse;

public interface ChatService {

    ChatResponse askQuestion(ChatRequest request);
}