package com.demo.springai.model;

import java.util.List;

public record Analysis(
        String summary,
        List<String> keyPoints,
        String verdict
) {}
