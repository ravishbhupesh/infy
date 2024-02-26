package com.infosys.poc.genai.chatbot.app.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private String model;
    private List<Message> messages;
}
