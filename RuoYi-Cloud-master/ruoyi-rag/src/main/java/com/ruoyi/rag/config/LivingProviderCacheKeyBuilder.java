package com.ruoyi.rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LivingProviderCacheKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String SPLIT_ITEM = ":";
    private static String LIVING_ROOM_OBJ = "rag_chat_obj";
    private static String LIVING_ROOM_USER_LIST = "rag_chat_user_list";


    public String buildLivingRoomUserSet(Integer roomId, Integer appId) {
        return getPrefix() + LIVING_ROOM_USER_LIST + SPLIT_ITEM + appId + SPLIT_ITEM + roomId;
    }

    public String buildLivingRoomObj(Long roomId) {
        return getPrefix() + LIVING_ROOM_OBJ + SPLIT_ITEM + roomId;
    }


    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }

}
