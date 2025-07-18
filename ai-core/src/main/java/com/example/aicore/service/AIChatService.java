package com.example.aicore.service;

import com.example.aicore.bean.aichat.req.PromptInfo;

/**
 * 对话执行
 *
 * @author summerice
 * @since 2025-07-18 15:45:25
 */
public interface AIChatService {

    String chatAny(PromptInfo prePrompt);
}
