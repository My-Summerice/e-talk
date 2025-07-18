package com.example.aicore.chatclient.doubao;

import com.example.aicore.config.DoubaoProperties;
import com.volcengine.ark.runtime.model.images.generation.GenerateImagesRequest;
import com.volcengine.ark.runtime.model.images.generation.ImagesResponse;
import com.volcengine.ark.runtime.service.ArkService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author summerice
 * @since 2025-07-17 14:58:27
 */
@Slf4j
@Component("doubao-image-model")
public class DoubaoImageClient implements ChatClient {

    @Autowired
    private DoubaoProperties properties;

    @Override
    public ChatResponse call(Prompt prompt) {

        String apiKey = properties.getApiKey();
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).apiKey(apiKey).build();

        GenerateImagesRequest generateRequest = GenerateImagesRequest.builder()
                .model(properties.getModelImage())
                .prompt(prompt.getInstructions().stream().map(Message::getContent).collect(Collectors.joining("\n")))
                .build();

        ImagesResponse imagesResponse = service.generateImages(generateRequest);
        String url = imagesResponse.getData().get(0).getUrl();
        log.info("豆包大模型生成图片返回{}", url);

        // 关闭API连接
        service.shutdownExecutor();
        Generation generation = new Generation(url);
        return new ChatResponse(Collections.singletonList(generation));
    }
}
