package com.example.aicore.chatclient;

import com.alibaba.fastjson.JSONObject;
import com.example.aicore.config.DoubaoProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author summerice
 * @since 2025-07-16 16:12:00
 */
@Slf4j
@Component
public class DoubaoChatClient implements ChatClient {

    @Autowired
    private DoubaoProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ChatResponse call(Prompt prompt) {
        // 构造 Doubao 的请求 JSON
        Map<String, Object> body = Map.of(
                "model", properties.getModel(),
                "messages", prompt.getInstructions().stream().map(instruction ->
                        Map.of("role", "user", "content", instruction.getContent())
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
