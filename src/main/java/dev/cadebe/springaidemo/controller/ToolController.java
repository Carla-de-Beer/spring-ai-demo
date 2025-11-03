package dev.cadebe.springaidemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.utils.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@RestController
@RequestMapping(ToolController.API_PREFIX)
public class ToolController {

    static final String API_PREFIX = "/api";
    private static final String STRUCTURED_OUTPUT_PATH = "/service-request";

    private final ChatClient chatClient;

    public ToolController(@Qualifier("toolClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping(STRUCTURED_OUTPUT_PATH)
    public ResponseEntity<String> handleServiceRequest(@RequestHeader("username") String username,
                                                       @RequestParam String message) {
        if (StringUtils.isBlank(username)) {
            return ResponseEntity.badRequest().body("Missing required header 'username'");
        }

        if (StringUtils.isBlank(message)) {
            return ResponseEntity.badRequest().body("Missing required parameter 'message'");
        }

        try {
            String response = chatClient
                    .prompt()
                    .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                    .user(message)
                    .tools()
                    .toolContext(Map.of("requesterName", username))
                    .call()
                    .content();

            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            log.error("Error handling service request for user {}: {}", username, exception.getMessage(), exception);
            return ResponseEntity.status(500).body("Error processing request: " + exception.getMessage());
        }
    }
}
