package com.example.aicore.bean.aichat.req;

import lombok.Data;

/**
 * @author summerice
 * @since 2025-07-18 15:54:46
 */
@Data
public class PromptInfo {

    private String intent;

    private String format;

    private String reasoning;

    private double confidence;

    private String prompt;

    private String model;
}
