package dev.cadebe.springaidemo.config.springai;

import dev.cadebe.springaidemo.advisors.TokenUsageAuditAdvisor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final TemplateConfig templateConfig;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        // Configured for Mistral (model-specific settings)
        ChatOptions chatOptions = ChatOptions.builder()
                .maxTokens(2000)
                .temperature(0.8)
                .topP(0.9)
                .build();

        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
                // Default across all chats but can be overwritten
                .defaultSystem(templateConfig.getSystemTemplates().getInsuranceCompanyTemplate())
                // Default across all chats but can be overwritten
                .defaultUser("I need assistance with my query")
                .build();
    }
}