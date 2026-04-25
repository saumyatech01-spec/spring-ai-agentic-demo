package com.demo.springai.model;

public record ChatResponse(
        String response,
        String sessionId
) {}
