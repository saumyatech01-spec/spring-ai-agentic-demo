package com.demo.springai.controller;

import com.demo.springai.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rag")
@Tag(name = "RAG", description = "Retrieval Augmented Generation Q&A endpoint")
public class RagController {

  private final RagService ragService;

  public RagController(RagService ragService) {
    this.ragService = ragService;
  }

  @PostMapping("/query")
  @Operation(summary = "RAG Q&A query", description = "Answers questions grounded in the embedded knowledge base")
  public ResponseEntity<Map<String, String>> query(@RequestBody Map<String, String> body) {
    String question = body.getOrDefault("question", "");
    log.info("POST /api/rag/query - question: {}", question);
    String answer = ragService.query(question);
    return ResponseEntity.ok(Map.of("question", question, "answer", answer));
  }
}
