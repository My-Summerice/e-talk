package com.example.aicore.constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息类型枚举
 *
 * @author summerice
 * @since 2025-07-18 15:34:09
 */
@Slf4j
@Getter
public enum MessageTypeEnum {

    TEXT("text", "文本"),
    IMAGE("image", "图片"),
    VIDEO("video", "视频"),
    AUDIO("audio", "音频"),
    ;

    MessageTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final String code;
    private final String desc;

    public static MessageTypeEnum getByCode(String code) {

        for (MessageTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        log.error("Invalid message type code: {}", code);
        return TEXT;
    }
}
