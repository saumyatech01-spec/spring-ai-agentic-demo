package com.demo.springai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WebSearchTool {

    private static final Map<String, String> MOCK_SEARCH_RESULTS = Map.of(
            "java 21", "Java 21 is an LTS release featuring virtual threads, pattern matching, and record patterns. Released September 2023.",
            "spring ai", "Spring AI 1.0 GA provides abstractions for AI integration in Spring Boot. Supports OpenAI, Anthropic, Bedrock, and more.",
            "openai gpt-4o", "GPT-4o is OpenAI's multimodal flagship model. Supports text, image, and audio inputs with improved speed and cost efficiency.",
            "spring boot 3", "Spring Boot 3.x requires Java 17+, uses native compilation via GraalVM, and includes improved observability with Micrometer."
    );

    @Tool(description = "Search the web for recent information about a topic. Returns relevant snippets and summaries about the query.")
    public String search(String query) {
        log.debug("WebSearchTool called for query: {}", query);
        String normalizedQuery = query.toLowerCase().trim();
        return MOCK_SEARCH_RESULTS.entrySet().stream()
                .filter(e -> normalizedQuery.contains(e.getKey()) || e.getKey().contains(normalizedQuery))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Search results for '" + query + "': This is a mock web search. In production, " +
                        "integrate with a real search API like Tavily, SerpAPI, or Brave Search. " +
                        "The query '" + query + "' returned no cached mock results.");
    }
}
