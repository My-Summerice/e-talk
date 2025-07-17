package com.example.aicore.controller;

import com.example.aicore.chatclient.DoubaoChatClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ai/chat")
public class ChatController {

    @Autowired
    private DoubaoChatClient doubaoClient;

    @GetMapping("/doubao")
    public String chat(@RequestParam String prompt) {

        try {
            log.info("Received prompt: {}", prompt);
            return doubaoClient.call(prompt);
        } catch (Exception e) {
            log.error("Error processing prompt: {}", e.getMessage());
            return null;
        }
    }
}