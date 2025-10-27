package dev.cadebe.springaidemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(StreamingController.API_PREFIX)
@RequiredArgsConstructor
public class StreamingController {

    static final String API_PREFIX = "/api";

    private final ChatClient chatClient;

    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .stream() // stream the response
                .content();
    }
}