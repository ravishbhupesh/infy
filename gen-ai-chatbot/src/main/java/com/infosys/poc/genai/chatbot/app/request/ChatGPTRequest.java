package com.infosys.poc.genai.chatbot.app.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTRequest {

    private String model;
    private List<ChatGPTRequestMessage> messages;
}
