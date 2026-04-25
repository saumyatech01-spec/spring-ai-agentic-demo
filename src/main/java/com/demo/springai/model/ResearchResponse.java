package com.demo.springai.model;

import java.util.List;

public record ResearchResponse(
        String finalAnswer,
        List<ReasoningStep> steps,
        int totalIterations
) {
    public record ReasoningStep(
            int iteration,
            String thought,
            String toolCalled,
            String toolArguments,
            String toolResult
    ) {}
}
