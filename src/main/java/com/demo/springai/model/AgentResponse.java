package com.demo.springai.model;

import java.util.List;

public record AgentResponse(
        String response,
        List<String> toolCallTrace
) {}
