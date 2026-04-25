package com.demo.springai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class WeatherTool {

    private static final Map<String, Integer> MOCK_TEMPERATURES = Map.of(
            "bangalore", 28,
            "mumbai", 32,
            "delhi", 25,
            "chennai", 35,
            "hyderabad", 30,
            "london", 15,
            "new york", 22,
            "tokyo", 18
    );

    @Tool(description = "Get the current weather and temperature for a given city. Returns temperature in Celsius and weather condition.")
    public String getWeather(String city) {
        log.debug("WeatherTool called for city: {}", city);
        String normalizedCity = city.toLowerCase().trim();
        int temperature = MOCK_TEMPERATURES.getOrDefault(normalizedCity,
                20 + new Random().nextInt(15));
        String[] conditions = {"Sunny", "Partly Cloudy", "Cloudy", "Humid"};
        String condition = conditions[new Random().nextInt(conditions.length)];
        return String.format("{\"city\": \"%s\", \"temperature\": %d, \"unit\": \"Celsius\", \"condition\": \"%s\"}",
                city, temperature, condition);
    }
}
