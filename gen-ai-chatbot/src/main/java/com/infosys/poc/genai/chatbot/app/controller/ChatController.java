package com.infosys.poc.genai.chatbot.app.controller;

import com.infosys.poc.genai.chatbot.app.dto.MessageInput;
import com.infosys.poc.genai.chatbot.app.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody MessageInput messageInput) {
        log.info("Request received : {}", messageInput.toString());
        String response =  chatService.sendMessage(messageInput.getMessage());
        log.info("Response sent : {}", response);
        return response;
    }
}