package com.demo.springai.model;

import jakarta.validation.constraints.NotBlank;

public record AgentRequest(
        @NotBlank(message = "Task cannot be blank")
        String task
) {}
