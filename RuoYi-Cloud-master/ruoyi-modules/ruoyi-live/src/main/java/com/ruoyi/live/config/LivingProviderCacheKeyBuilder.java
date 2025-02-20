package com.ruoyi.live.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LivingProviderCacheKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String SPLIT_ITEM = ":";
    private static String LIVING_ROOM_OBJ = "living_room_obj";
    private static String LIVING_ROOM_LIST = "living_room_list";
    private static String LIVING_ROOM_USER_LIST = "living_room_user_list";


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
