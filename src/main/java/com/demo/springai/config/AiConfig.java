package com.demo.springai.config;

import com.demo.springai.tools.CalculatorTool;
import com.demo.springai.tools.WeatherTool;
import com.demo.springai.tools.WebSearchTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AiConfig {

    @Bean
    @Primary
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a helpful AI assistant. Be concise and accurate.")
                .build();
    }

    @Bean
    @Qualifier("agentChatClient")
    public ChatClient agentChatClient(
            ChatClient.Builder builder,
            WeatherTool weatherTool,
            CalculatorTool calculatorTool,
            WebSearchTool webSearchTool) {
        return builder
                .defaultSystem("""
                        You are an intelligent agent. Use the available tools to
                        answer user questions. Always prefer tools for real-time
                        or computed data. Think step by step before calling tools.
                        """)
                .defaultTools(weatherTool, calculatorTool, webSearchTool)
                .build();
    }
}
