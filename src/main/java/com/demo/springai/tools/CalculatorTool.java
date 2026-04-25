package com.demo.springai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalculatorTool {

    @Tool(description = "Perform basic arithmetic operations: add, subtract, multiply, divide. Provide operation as string and two numbers.")
    public double calculate(String operation, double a, double b) {
        log.debug("CalculatorTool: {} {} {}", operation, a, b);
        return switch (operation.toLowerCase().trim()) {
            case "add", "+" -> a + b;
            case "subtract", "-" -> a - b;
            case "multiply", "*", "x" -> a * b;
            case "divide", "/" -> {
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown operation: " + operation +
                    ". Supported: add, subtract, multiply, divide");
        };
    }
}
