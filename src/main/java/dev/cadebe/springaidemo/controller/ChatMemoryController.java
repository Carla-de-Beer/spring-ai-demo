package dev.cadebe.springaidemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping(ChatMemoryController.API_PREFIX)
public class ChatMemoryController {

    static final String API_PREFIX = "/api";

    private final ChatClient chatClient;

    public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat-memory")
    public ResponseEntity<String> chatMemory(@RequestHeader("username") String username, @RequestParam("message") String message) {

        String result = chatClient
                .prompt()
                .user("""
                          Write as much as possible within the model’s response limit,
                          ensuring the answer is complete and coherent (do not cut off mid-sentence).
                        """ + message)
                // CONVERSATION_ID: links the "memory" to the correct user
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .call()
                .content(); // just the response as string
        // .chatClientResponse(); // if you wish to output the metadata to the prompt - usefuul for RAG
        // call together with the content

        return ResponseEntity.ok(result);
    }

}