package com.example.aicore.service;

import com.example.aicore.bean.aichat.req.PromptInfo;

/**
 * 语义分析
 *
 * @author summerice
 * @since 2025-07-18 15:49:32
 */
public interface SemanticAnalysisService {

    PromptInfo semanticAnalysis(String prompt);
}
