package dev.cadebe.springaidemo.controller;

import dev.cadebe.springaidemo.springai.config.TemplateConfig;
import dev.cadebe.springaidemo.model.FruitTreeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(StructuredOutputController.API_PREFIX)
public class StructuredOutputController {

    static final String API_PREFIX = "/api";
    static final String STRUCTURED_OUTPUT_PATH = "/structured-output";

    private final ChatClient chatClient;
    private final TemplateConfig templateConfig;

    public StructuredOutputController(@Qualifier("structuredOutputClient") ChatClient chatClient,
                                      TemplateConfig templateConfig) {
        this.chatClient = chatClient;
        this.templateConfig = templateConfig;
    }

    @SuppressWarnings("java:S1452")
    @GetMapping(STRUCTURED_OUTPUT_PATH)
    public ResponseEntity<?> chat(@NotNull @RequestParam("location") String location) {
        if (location.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Provide a valid geographic location.");
        }

        String match = chatClient
                .prompt()
                .user(location)
                .user(promptTemplateSpec -> promptTemplateSpec
                        .text(templateConfig.getSystemTemplates().getFruitTreeLocationTemplate())
                        .param("location", location))
                .call()
                .content();

        if (match != null && match.trim().contains("NO")) {
            return ResponseEntity.badRequest()
                    .body("No fruit tree information available for this location. Enter a valid geographic location.");
        }

        try {
            List<FruitTreeInfo> info = chatClient
                    .prompt()
                    .user(promptTemplateSpec -> promptTemplateSpec
                            .text(templateConfig.getSystemTemplates().getFruitTreeInfoTemplate()))
                    .user(location)
                    .call()
                    .entity(new ParameterizedTypeReference<>() {
                    });

            return ResponseEntity.ok(info);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred");
        }
    }

}
