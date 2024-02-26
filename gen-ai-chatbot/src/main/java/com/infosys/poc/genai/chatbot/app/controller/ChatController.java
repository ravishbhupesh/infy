package com.infosys.poc.genai.chatbot.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.poc.genai.chatbot.app.dto.MessageInput;
import com.infosys.poc.genai.chatbot.app.dto.MessageResponse;
import com.infosys.poc.genai.chatbot.app.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageInput messageInput) {
        try {
            log.info("Request received : {}", objectMapper.writeValueAsString(messageInput));

            String response =  chatService.sendMessage(messageInput.getMessage());
            log.info("Response from service : {}", response);

            MessageResponse responseObj = new MessageResponse(response);
            log.info("Response sent : {}", objectMapper.writeValueAsString(responseObj));

            return ResponseEntity.ok(responseObj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}