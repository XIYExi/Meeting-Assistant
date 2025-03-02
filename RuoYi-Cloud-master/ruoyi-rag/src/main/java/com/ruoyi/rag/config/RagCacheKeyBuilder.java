package com.ruoyi.rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagCacheKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String SPLIT_ITEM = ":";
    private static String RAG_CHATMEMORY_KEY = "imAckMap";

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }

    /**
     * 按照用户id构造具体key
     * @param userId
     * @return
     */
    public String buildRagChatMemoryKey(Long userId) {
        return getPrefix() + RAG_CHATMEMORY_KEY + SPLIT_ITEM + userId;
    }


}
