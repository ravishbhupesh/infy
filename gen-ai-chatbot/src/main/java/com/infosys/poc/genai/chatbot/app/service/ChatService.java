package com.infosys.poc.genai.chatbot.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.poc.genai.chatbot.app.request.ChatGPTRequest;
import com.infosys.poc.genai.chatbot.app.request.ChatGPTRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {

    private static final String CHAT_GPT_MODEL = "gpt-3.5-turbo-0125";
    //private static final String CHAT_GPT_MODEL = "gpt-3.5-turbo";
    @Value("${chatgpt.apiKey}") // You can use application.yml to store the API key
    private String apiKey;

    private final String chatGptEndpoint = "https://api.openai.com/v1/completions"; // Change this if needed

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public String sendMessage(String message) {
        log.debug("ChatService::sendMessage START");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            ChatGPTRequest request = createRequest(List.of(message));
            String requestBody = objectMapper.writeValueAsString(request);
            log.info("Request created for openAi : {}", requestBody);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.exchange(chatGptEndpoint, HttpMethod.POST, entity, String.class).getBody();
            log.debug("open ai raw response : {}", response);

            JsonNode jsonResponse = objectMapper.readTree(response);
            return jsonResponse.get("choices").get(0).get("text").asText();
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
            return "An error occurred while processing your message.";
        } finally {
            log.debug("ChatService::sendMessage END");
        }
    }

    private ChatGPTRequest createRequest(List<String> message) {
        ChatGPTRequest request = new ChatGPTRequest();
        request.setModel(CHAT_GPT_MODEL);

        request.setMessages(message.stream()
                .map(m -> new ChatGPTRequestMessage("user", m))
                .collect(Collectors.toList()));

        return request;
    }
}