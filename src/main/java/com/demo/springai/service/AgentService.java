package com.demo.springai.service;

import com.demo.springai.model.*;
import com.demo.springai.tools.CalculatorTool;
import com.demo.springai.tools.WeatherTool;
import com.demo.springai.tools.WebSearchTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AgentService {

  private final ChatClient agentChatClient;
  private final WeatherTool weatherTool;
  private final CalculatorTool calculatorTool;
  private final WebSearchTool webSearchTool;
  private final ToolCallingManager toolCallingManager;

  public AgentService(ChatClient agentChatClient,
                      WeatherTool weatherTool,
                      CalculatorTool calculatorTool,
                      WebSearchTool webSearchTool,
                      ToolCallingManager toolCallingManager) {
    this.agentChatClient = agentChatClient;
    this.weatherTool = weatherTool;
    this.calculatorTool = calculatorTool;
    this.webSearchTool = webSearchTool;
    this.toolCallingManager = toolCallingManager;
  }

  public AgentResponse runAgent(AgentRequest request) {
    log.info("Running agent for: {}", request.message());
    String response = agentChatClient.prompt()
        .user(request.message())
        .call()
        .content();
    return new AgentResponse(response, List.of());
  }

  public Analysis analyzeTopicStructured(String topic) {
    log.info("Structured analysis for: {}", topic);
    PromptTemplate template = new PromptTemplate(
        "Analyze the topic '{topic}' and provide: summary, keyPoints (3 items), sentiment (positive/negative/neutral), and confidence (0.0-1.0)."
    );
    String response = agentChatClient.prompt()
        .user(template.render(Map.of("topic", topic)))
        .call()
        .content();
    return new Analysis(topic, response, List.of("Key point 1", "Key point 2", "Key point 3"), "neutral", 0.85);
  }

  public ResearchResponse research(ResearchRequest request) {
    log.info("Starting ReAct research for: {}", request.query());
    List<ResearchResponse.ReasoningStep> steps = new ArrayList<>();
    List<Message> messages = new ArrayList<>();
    messages.add(new UserMessage(
        "You are a research assistant. Use available tools to answer: " + request.query()
    ));

    ToolCallback[] toolCallbacks = ToolCallbacks.from(weatherTool, calculatorTool, webSearchTool);
    int maxIterations = 5;
    int iteration = 0;
    String finalAnswer = null;

    while (iteration < maxIterations && finalAnswer == null) {
      iteration++;
      log.debug("ReAct iteration {}", iteration);
      ToolCallingChatOptions options = ToolCallingChatOptions.builder()
          .toolCallbacks(toolCallbacks)
          .internalToolExecutionEnabled(false)
          .build();
      org.springframework.ai.chat.prompt.Prompt prompt =
          new org.springframework.ai.chat.prompt.Prompt(messages, options);
      ChatResponse chatResponse = agentChatClient.prompt(prompt).call().chatResponse();
      AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
      messages.add(assistantMessage);

      if (assistantMessage.hasToolCalls()) {
        ToolExecutionResult toolResult = toolCallingManager.executeToolCalls(prompt, chatResponse);
        for (var toolCall : assistantMessage.getToolCalls()) {
          String resultContent = toolResult.conversationHistory().stream()
              .filter(m -> m instanceof ToolResponseMessage)
              .map(m -> ((ToolResponseMessage) m).getResponses().stream()
                  .map(ToolResponseMessage.ToolResponse::responseData)
                  .findFirst().orElse("no result"))
              .findFirst().orElse("executed");
          steps.add(new ResearchResponse.ReasoningStep(
              iteration, "LLM chose tool: " + toolCall.name(),
              toolCall.name(), toolCall.arguments(), resultContent));
        }
        messages.addAll(toolResult.conversationHistory().stream()
            .filter(m -> m instanceof ToolResponseMessage).toList());
      } else {
        finalAnswer = assistantMessage.getText();
      }
    }

    if (finalAnswer == null) finalAnswer = "Max iterations reached. Please refine your question.";
    return new ResearchResponse(finalAnswer, steps);
  }
}
