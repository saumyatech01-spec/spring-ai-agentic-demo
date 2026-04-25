package com.demo.springai.model;

import jakarta.validation.constraints.NotBlank;

public record AnalyzeRequest(
        @NotBlank(message = "Topic cannot be blank")
        String topic
) {}
