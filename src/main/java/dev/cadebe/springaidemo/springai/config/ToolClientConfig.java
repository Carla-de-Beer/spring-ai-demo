package dev.cadebe.springaidemo.springai.config;

import dev.cadebe.springaidemo.springai.advisors.TokenUsageAuditAdvisor;
import dev.cadebe.springaidemo.springai.tools.ServiceRequestTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ToolClientConfig {

    private final TemplateConfig templateConfig;
    private final ServiceRequestTool serviceRequestTool;

    @Bean("toolClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
                .defaultSystem(templateConfig.getSystemTemplates().getServiceRequestTemplate())
                .defaultTools(serviceRequestTool)
                .defaultUser("I need assistance with my IT service request")
                .build();
    }

}