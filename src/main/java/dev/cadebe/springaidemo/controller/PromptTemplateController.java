package dev.cadebe.springaidemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.cadebe.springaidemo.config.springai.TemplateConfig;

@RestController
@RequiredArgsConstructor
@RequestMapping(PromptTemplateController.API_PREFIX)
public class PromptTemplateController {

    static final String API_PREFIX = "/api";

    private final ChatClient chatClient;
    private final TemplateConfig templateConfig;

    @GetMapping("/email")
    public String emailResponse(@RequestParam("customerName") String customerName,
                                @RequestParam("customerMessage") String customerMessage) {
        return chatClient
                .prompt()
                .system("""
                        You are a professional customer service assistant that drafts email
                        responses to improve the productivity of the customer support team
                        """)
                .user(promptTemplateSpec ->
                        promptTemplateSpec.text(templateConfig.getUserTemplates().getEmailTemplate())
                                .param("customerName", customerName)        // parameter to prompt template
                                .param("customerMessage", customerMessage)) // parameter to prompt template
                .call()
                .content();
    }

}