package com.demo.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RagService {

  private static final String KNOWLEDGE_BASE =
      "Spring AI is a framework for building AI-powered applications in Java. " +
      "It provides abstractions over various AI models including OpenAI GPT-4. " +
      "Spring AI supports chat completion, embeddings, image generation, and tool calling. " +
      "Agentic AI systems can autonomously decide which tools to use to complete tasks. " +
      "The ReAct pattern combines reasoning and acting in iterative loops. " +
      "RAG stands for Retrieval Augmented Generation - it grounds LLM responses in real documents. " +
      "Spring Boot 3.4 requires Java 17+ and supports virtual threads via Project Loom. " +
      "OpenAI GPT-4o is a multimodal model supporting text, vision, and function calling.";

  private final ChatClient chatClient;

  public RagService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public String query(String question) {
    log.info("RAG query: {}", question);
    String prompt = String.format(
        "Use the following context to answer the question.\n\nContext:\n%s\n\nQuestion: %s\n\nAnswer based only on the provided context. If the answer is not in the context, say so.",
        KNOWLEDGE_BASE, question
    );
    return chatClient.prompt()
        .user(prompt)
        .call()
        .content();
  }
}
