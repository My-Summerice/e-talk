package com.example.aicore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.aicore.bean.aichat.req.PromptInfo;
import com.example.aicore.service.AIChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author summerice
 * @since 2025-07-18 15:46:21
 */
@Slf4j
@Service
public class AIChatServiceImpl implements AIChatService {

    @Autowired
    private Map<String, ChatClient> chatClientMap;

    @Override
    public String chatAny(PromptInfo promptInfo) {

        ChatClient chatClient = chatClientMap.get(promptInfo.getModel());

        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(promptInfo.getPrompt()));
        Prompt prompt = new Prompt(messages);

        log.info("请求：{}", JSONObject.toJSONString(prompt));
        Generation generation = chatClient.call(prompt).getResult();
        log.info("返回：{}", JSONObject.toJSONString(generation));

        return generation != null ? generation.getOutput().getContent() : "我不道啊";
    }
}
