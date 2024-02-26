package com.infosys.poc.genai.chatbot.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.poc.genai.chatbot.app.http.Request;
import com.infosys.poc.genai.chatbot.app.http.Response;
import com.infosys.poc.genai.chatbot.app.http.Message;
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

    @Value("${app.openai.model}")
    private String openaiModel;
    @Value("${app.openai.apiKey}")
    private String apiKey;

    @Value("${app.openai.url}")
    private String openaiUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public String sendMessage(String message) {
        log.info("ChatService::sendMessage START");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Request request = createRequest(List.of(message));
            String requestBody = objectMapper.writeValueAsString(request);
            log.info("Request created for openAi : {}", requestBody);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            String responseRaw = restTemplate.exchange(openaiUrl, HttpMethod.POST, entity, String.class).getBody();
            log.info("open ai raw response : {}", responseRaw);

            Response response = objectMapper.readValue(responseRaw, Response.class);

            log.info("Object response : {}", response);

            return (null != response) ? response.getChoices().get(0).getMessage().getContent() : "null";
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
            return "An error occurred while processing your message.";
        } finally {
            log.info("ChatService::sendMessage END");
        }
    }

    private Request createRequest(List<String> message) {
        Request request = new Request();
        request.setModel(openaiModel);

        request.setMessages(message.stream().map(m -> new Message("user", m)).collect(Collectors.toList()));

        return request;
    }
}