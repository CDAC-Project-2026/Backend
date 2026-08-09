package com.examportal.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.examportal.dtos.ChatRequest;
import com.examportal.dtos.ChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    
    @Override
    public ChatResponse askQuestion(ChatRequest request) {

        try {

            // Convert Java object to JSON
            String json = objectMapper.writeValueAsString(request);

            System.out.println("Sending to FastAPI: " + json);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8000/ask"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println("FastAPI status: " + response.statusCode());
            System.out.println("FastAPI response: " + response.body());

            if (response.statusCode() >= 400) {
                throw new RuntimeException(
                        "FastAPI returned " +
                        response.statusCode() +
                        ": " +
                        response.body()
                );
            }

            return objectMapper.readValue(
                    response.body(),
                    ChatResponse.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not communicate with chatbot service",
                    e
            );
        }
    }
}