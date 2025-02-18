package com.ruoyi.im.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImCoreServerCacheKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String SPLIT_ITEM = ":";

    private static String IM_ONLINE_ZSET = "imOnlineZset";


    private static String IM_ACK_MAP = "imAckMap";

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }

    /**
     * 按照用户id取模10000 得出具体key的位置
     * @param userId
     * @return
     */
    public String buildImLoginTokenKey(Long userId, Integer appId) {
        return getPrefix() + IM_ONLINE_ZSET + SPLIT_ITEM + appId + SPLIT_ITEM + userId % 10000;
    }

    public String buildImAckMapKey(Long userId,Integer appId) {
        return getPrefix() + IM_ACK_MAP + SPLIT_ITEM + appId + SPLIT_ITEM + userId % 100;
    }

}
