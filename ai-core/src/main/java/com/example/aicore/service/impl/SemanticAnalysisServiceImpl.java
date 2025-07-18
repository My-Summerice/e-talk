package com.example.aicore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.aicore.bean.aichat.req.PromptInfo;
import com.example.aicore.chatclient.doubao.DoubaoChatClient;
import com.example.aicore.service.SemanticAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author summerice
 * @since 2025-07-18 15:50:33
 */
@Slf4j
@Service
public class SemanticAnalysisServiceImpl implements SemanticAnalysisService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DoubaoChatClient doubaoChatClient;

    @Override
    public PromptInfo semanticAnalysis(String question) {

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(getSystemPrompt()));
        messages.add(new UserMessage(question));
        Prompt prompt = new Prompt(messages);

        log.info("语义分析请求：{}", JSONObject.toJSONString(prompt));
        Generation generation = doubaoChatClient.call(prompt).getResult();
        log.info("语义分析返回：{}", JSONObject.toJSONString(generation));

        PromptInfo analysisResult = generation != null ? JSONObject.parseObject(generation.getOutput().getContent(), PromptInfo.class) : new PromptInfo();
        analysisResult.setPrompt(question);

        return analysisResult;
    }

    private String getSystemPrompt() {

        return "你是一个专业的意图分析引擎，需要对用户输入的自然语言进行分析，提取以下信息并返回JSON格式：\n" + "1. intent：用户的主要意图（如：闲聊、提问、代码生成、图像生成、音频生成、视频生成、数据查询、报表导出、知识问答）。\n" + "2. format：期望的输出类型（text、json、image、audio、video）。\n" + "3. confidence：你的判断置信度（0-1之间）。\n" + "4. reasoning：简要说明你的推理逻辑。\n" + "5. model：根据用户意图，推荐的模型名称（从提供的候选列表中选择）。\n" + "\n" + "要求：\n" + "- 严格返回JSON格式，不包含任何多余文字。\n" + "- 如果用户的问题不明确，请猜测最可能的意图。\n" + "\n" + "可用模型列表（从中选择model）：\n" + getAllBeanNamesByType() + "\n\n" + "输出JSON示例：\n" + "{\n" + "  \"intent\": \"code_generation\",\n" + "  \"format\": \"text\",\n" + "  \"confidence\": 0.95,\n" + "  \"reasoning\": \"用户提到Java代码和实现逻辑，属于代码生成任务。\",\n" + "  \"model\": \"GPT-4\"\n" + "}";
    }

    /**
     * 获取所有model bean name
     */
    public List<String> getAllBeanNamesByType() {

        String[] beanNames = applicationContext.getBeanNamesForType(ChatClient.class);
        return Arrays.stream(beanNames)
                .filter(StringUtils::isNotBlank)
                .filter(x -> StringUtils.startsWithIgnoreCase(x, "doubao"))
                .collect(Collectors.toList());
    }
}
