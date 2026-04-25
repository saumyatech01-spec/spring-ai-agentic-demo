package com.demo.springai.controller;

import com.demo.springai.model.ChatRequest;
import com.demo.springai.model.ChatResponse;
import com.demo.springai.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "Basic chat and streaming endpoints")
public class ChatController {

  private final ChatService chatService;

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @PostMapping
  @Operation(summary = "Send a chat message", description = "Accepts a message and optional sessionId, returns AI response with conversation memory")
  public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
    log.info("POST /api/chat - sessionId: {}", request.sessionId());
    return ResponseEntity.ok(chatService.chat(request));
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @Operation(summary = "Stream a chat response", description = "Streams AI response as Server-Sent Events")
  public Flux<String> stream(@RequestParam String message,
                             @RequestParam(required = false) String sessionId) {
    log.info("GET /api/chat/stream - message: {}", message);
    return chatService.streamChat(message, sessionId);
  }
}
