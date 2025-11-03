package dev.cadebe.springaidemo.springai.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StructuredOutputClientConfig {

    @Bean("structuredOutputClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        ChatOptions chatOptions = ChatOptions.builder()
                .maxTokens(1500)
                .temperature(0.7)
                .build();

        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
                .build();
    }
}
