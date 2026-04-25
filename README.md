# spring-ai-agentic-demo

Agentic AI application showcasing **Spring AI + OpenAI** integration. Demonstrates chat with memory, tool-calling agents, structured output, multi-turn ReAct workflows, and RAG — all in a single Spring Boot 3.4 application.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.4.x |
| AI Framework | Spring AI 1.0.x |
| LLM | OpenAI GPT-4o |
| Build | Maven |
| Docs | Springdoc OpenAPI / Swagger UI |

## Prerequisites

- Java 21+
- Maven 3.9+
- OpenAI API Key

## Running the Application

```bash
# Clone the repository
git clone https://github.com/saumyatech01-spec/spring-ai-agentic-demo.git
cd spring-ai-agentic-demo

# Set your OpenAI API key
export OPENAI_API_KEY=sk-your-api-key-here

# Run with Maven
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/chat` | Chat with memory (sessionId optional) |
| GET | `/api/chat/stream` | Streaming chat via SSE |
| POST | `/api/agent/run` | Agentic task with tool calling |
| POST | `/api/agent/analyze` | Structured topic analysis |
| POST | `/api/agent/research` | Multi-turn ReAct research loop |
| POST | `/api/rag/query` | RAG Q&A over knowledge base |

## Swagger UI

Open `http://localhost:8080/swagger-ui.html` to explore and test all endpoints interactively.

## Example Requests

### Chat
```json
POST /api/chat
{
  "message": "What is the capital of France?",
  "sessionId": "session-001"
}
```

### Agent Run (tool calling)
```json
POST /api/agent/run
{
  "message": "What is the weather in London and what is 25 * 4?"
}
```

### ReAct Research
```json
POST /api/agent/research
{
  "query": "What is the current weather in Tokyo and summarize recent AI news?"
}
```

### RAG Query
```json
POST /api/rag/query
{
  "question": "What is Spring AI?"
}
```

## Opening in IntelliJ IDEA

1. Clone the repository
2. In IntelliJ: **File → Open**
3. Select the `pom.xml` file
4. Choose **Open as Project**
5. Set **Project SDK** to Java 21
6. Add `OPENAI_API_KEY` to your run configuration environment variables
7. Run `SpringAiAgenticDemoApplication`

## Project Structure

```
src/main/java/com/demo/springai/
├── SpringAiAgenticDemoApplication.java
├── config/
│   └── AiConfig.java
├── controller/
│   ├── ChatController.java
│   ├── AgentController.java
│   └── RagController.java
├── service/
│   ├── ChatService.java
│   ├── AgentService.java
│   └── RagService.java
├── tools/
│   ├── WeatherTool.java
│   ├── CalculatorTool.java
│   └── WebSearchTool.java
└── model/
    ├── ChatRequest.java / ChatResponse.java
    ├── AgentRequest.java / AgentResponse.java
    ├── AnalyzeRequest.java / Analysis.java
    └── ResearchRequest.java / ResearchResponse.java
```

## License

MIT
