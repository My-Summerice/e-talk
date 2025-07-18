package com.example.aicore.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.aicore.bean.aichat.req.PromptInfo;
import com.example.aicore.chatclient.doubao.DoubaoChatClient;
import com.example.aicore.facade.AIChatFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author summerice
 * @since 2025-07-15 14:34:55
 * 这层的内容后续放到service层，controller层只负责接收、校验参数，facade做复杂逻辑流程控制，service层实现具体单一逻辑
 */
@Slf4j
@RestController
@RequestMapping("/ai/chat")
public class AIChatController {

    @Autowired
    private AIChatFacade aichatFacade;

    @Autowired
    private DoubaoChatClient doubaoClient;

    @PostMapping("/any")
    public String chatAny(@RequestBody PromptInfo req) {

        try {
            log.info("Received chatAny req: {}", JSONObject.toJSONString(req));
            return aichatFacade.chatAny(req);
        } catch (Exception e) {
            // todo 后续补充全局异常捕获，目前主要完成核心功能
            log.error("Error processing chatAny: ", e);
            return null;
        }
    }

    @GetMapping("/text")
    public String chatText(@RequestParam String prompt) {

        try {
            log.info("Received text prompt: {}", prompt);
            return doubaoClient.call(prompt);
        } catch (Exception e) {
            log.error("Error processing prompt: {}", e.getMessage());
            return null;
        }
    }

    @GetMapping("/image")
    public String chatImage(@RequestParam String prompt) {

        try {
            log.info("Received image prompt: {}", prompt);
            return doubaoClient.call(prompt);
        } catch (Exception e) {
            log.error("Error processing prompt: {}", e.getMessage());
            return null;
        }
    }
}