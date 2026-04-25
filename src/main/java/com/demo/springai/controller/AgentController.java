package com.demo.springai.controller;

import com.demo.springai.model.*;
import com.demo.springai.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/agent")
@Tag(name = "Agent", description = "Agentic AI endpoints with tool calling and ReAct loop")
public class AgentController {

  private final AgentService agentService;

  public AgentController(AgentService agentService) {
    this.agentService = agentService;
  }

  @PostMapping("/run")
  @Operation(summary = "Run agentic task", description = "Agent autonomously selects and chains tools to answer the query")
  public ResponseEntity<AgentResponse> runAgent(@RequestBody AgentRequest request) {
    log.info("POST /api/agent/run - message: {}", request.message());
    return ResponseEntity.ok(agentService.runAgent(request));
  }

  @PostMapping("/analyze")
  @Operation(summary = "Structured analysis", description = "Analyzes a topic using PromptTemplate and returns structured Analysis object")
  public ResponseEntity<Analysis> analyze(@RequestBody AnalyzeRequest request) {
    log.info("POST /api/agent/analyze - topic: {}", request.topic());
    return ResponseEntity.ok(agentService.analyzeTopicStructured(request.topic()));
  }

  @PostMapping("/research")
  @Operation(summary = "Multi-turn ReAct research", description = "Runs a ReAct loop (up to 5 iterations) using tools to answer the research query")
  public ResponseEntity<ResearchResponse> research(@RequestBody ResearchRequest request) {
    log.info("POST /api/agent/research - query: {}", request.query());
    return ResponseEntity.ok(agentService.research(request));
  }
}
