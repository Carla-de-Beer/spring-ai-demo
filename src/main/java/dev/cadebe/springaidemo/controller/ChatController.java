package dev.cadebe.springaidemo.controller;

import dev.cadebe.springaidemo.springai.config.TemplateConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ChatController.API_PREFIX)
public class ChatController {

    static final String API_PREFIX = "/api";
    private static final String CHAT_PATH = "/chat";

    private final ChatClient chatClient;
    private final TemplateConfig templateConfig;

    public ChatController(ChatClient.Builder chatClientBuilder, TemplateConfig templateConfig) {
        this.chatClient = chatClientBuilder.build();
        this.templateConfig = templateConfig;
    }

    @GetMapping(CHAT_PATH)
    public String chat(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .system(templateConfig.getSystemTemplates().getEstateAgentSearchTemplate())
                .user(message)
                .call()
                .content();
    }
}
