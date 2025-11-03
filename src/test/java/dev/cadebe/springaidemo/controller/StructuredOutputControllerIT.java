package dev.cadebe.springaidemo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cadebe.springaidemo.model.FruitTreeInfo;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static dev.cadebe.springaidemo.controller.StructuredOutputController.API_PREFIX;
import static dev.cadebe.springaidemo.controller.StructuredOutputController.STRUCTURED_OUTPUT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StructuredOutputControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatModel chatModel;

    private ChatClient chatClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
                .build();
    }

    @Test
    void contextLoads_withStructuredOutputClient() {
        assertThat(chatClient).isNotNull();
    }

    @Test
    @Timeout(10)
    void shouldReturnBadRequestForBlankLocation() throws Exception {
        mockMvc.perform(get(API_PREFIX + STRUCTURED_OUTPUT_PATH)
                        .param("location", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Provide a valid geographic location."));
    }

    @Test
    @Timeout(30)
    void shouldReturnBadRequestForInvalidLocation() throws Exception {
        mockMvc.perform(get(API_PREFIX + STRUCTURED_OUTPUT_PATH)
                        .param("location", "asdfghjkl"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "No fruit tree information available for this location. Enter a valid geographic location."));
    }

    @Test
    @Timeout(60)
    void shouldReturnStructuredJsonForValidLocation() throws Exception {
        val result = mockMvc.perform(get(API_PREFIX + STRUCTURED_OUTPUT_PATH)
                        .param("location", "California")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        val jsonResponse = result.getResponse().getContentAsString();

        assertThat(jsonResponse)
                .as("Response JSON should not be blank")
                .isNotBlank();

        try {
            val infoList = objectMapper.readValue(jsonResponse, new TypeReference<List<FruitTreeInfo>>() {
            });

            assertThat(infoList)
                    .as("Top-level fruit tree info list")
                    .isNotEmpty();

            val firstInfo = infoList.getFirst();

            assertThat(firstInfo.fruitTrees()).isNotEmpty();
        } catch (Exception exception) {
            throw new AssertionError("Response was not valid structured JSON: " + jsonResponse, exception);
        }
    }

}
