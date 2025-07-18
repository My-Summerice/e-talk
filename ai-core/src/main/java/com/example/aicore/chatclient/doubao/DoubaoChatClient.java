package com.example.aicore.chatclient.doubao;

import com.alibaba.fastjson.JSONObject;
import com.example.aicore.config.DoubaoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author summerice
 * @since 2025-07-16 16:12:00
 */
@Slf4j
@Component("doubao-text-model")
public class DoubaoChatClient implements ChatClient {

    @Autowired
    private DoubaoProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ChatResponse call(Prompt prompt) {

        // 构造 Doubao 的请求 JSON
        Map<String, Object> body = Map.of(
                "model", properties.getModelText(),
                "messages", prompt.getInstructions().stream().map(message ->
                        Map.of("role", message.getMessageType().getValue(), "content", message.getContent())
                ).toList()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        log.info("豆包大模型接口请求：{}", JSONObject.toJSONString(request));
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(properties.getBaseUrl() + "/api/v3/chat/completions", request, JSONObject.class);
        log.info("豆包大模型接口返回：{}", JSONObject.toJSONString(response));

        // 从 response 提取答案，封装成 ChatResponse
        String answer = extractContent(Objects.requireNonNull(response.getBody()));
        Generation generation = new Generation(answer);
        return new ChatResponse(Collections.singletonList(generation));
    }

    private String extractContent(JSONObject body) {

        // 根据 Doubao 的返回 JSON 提取内容
        return body.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}
