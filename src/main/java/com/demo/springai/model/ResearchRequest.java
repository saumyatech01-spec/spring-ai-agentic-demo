package com.demo.springai.model;

import jakarta.validation.constraints.NotBlank;

public record ResearchRequest(
        @NotBlank(message = "Question cannot be blank")
        String question
) {}
