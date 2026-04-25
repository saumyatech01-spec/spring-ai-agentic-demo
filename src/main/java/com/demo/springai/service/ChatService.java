package com.demo.springai.service;

import com.demo.springai.model.ChatRequest;
import com.demo.springai.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ConcurrentHashMap<String, InMemoryChatMemory> sessions = new ConcurrentHashMap<>();

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public ChatResponse chat(ChatRequest request) {
        String sessionId = (request.sessionId() == null || request.sessionId().isBlank())
                ? UUID.randomUUID().toString()
                : request.sessionId();
        log.debug("Chat request - sessionId: {}, message: {}", sessionId, request.message());
        InMemoryChatMemory memory = sessions.computeIfAbsent(sessionId, k -> new InMemoryChatMemory());
        String response = chatClient.prompt()
                .advisors(new MessageChatMemoryAdvisor(memory))
                .user(request.message())
                .call()
                .content();
        return new ChatResponse(response, sessionId);
    }

    public Flux<String> streamChat(String message, String sessionId) {
        String sid = (sessionId == null || sessionId.isBlank())
                ? UUID.randomUUID().toString()
                : sessionId;
        log.debug("Stream chat request - sessionId: {}", sid);
        InMemoryChatMemory memory = sessions.computeIfAbsent(sid, k -> new InMemoryChatMemory());
        return chatClient.prompt()
                .advisors(new MessageChatMemoryAdvisor(memory))
                .user(message)
                .stream()
                .content();
    }
}
