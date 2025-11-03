package dev.cadebe.springaidemo.controller;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatControllerIT {

    @Autowired
    private ChatController chatController;

    @Test
    @Timeout(value = 30)
    void evaluateChatControllerResponseRelevancy() {
        val question = "I want to buy a house with outdoor space that I can reach with public transport";

        val aiResponse = chatController.chat(question);

        assertThat(aiResponse)
                .as("AI response should contain all expected search terms")
                .containsAnyOf("BUY", "HOUSE", "GARDEN", "PUBLIC_TRANSPORT");
    }
}
