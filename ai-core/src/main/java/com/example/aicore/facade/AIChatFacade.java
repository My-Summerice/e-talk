package com.example.aicore.facade;

import com.example.aicore.bean.aichat.req.PromptInfo;
import com.example.aicore.service.AIChatService;
import com.example.aicore.service.SemanticAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author summerice
 * @since 2025-07-18 15:44:02
 */
@Slf4j
@Component
public class AIChatFacade {

    @Autowired
    private AIChatService aiChatService;

    @Autowired
    private SemanticAnalysisService semanticAnalysisService;

    public String chatAny(PromptInfo req) {

        // 1.语义分析
        PromptInfo prePrompt = semanticAnalysisService.semanticAnalysis(req.getPrompt());

        // 2.执行预处理请求
        String answer = aiChatService.chatAny(prePrompt);
        // 3.todo any other things
        handleAnswer(answer);

        return answer;
    }

    private void handleAnswer(String answer) {

        // 这里可以附加一些个性化的逻辑或系统化逻辑，如将用户chat上下文缓存至redis
        log.info("answer: {}", answer);
    }

}
