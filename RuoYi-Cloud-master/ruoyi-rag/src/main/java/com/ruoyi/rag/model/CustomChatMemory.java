package com.ruoyi.rag.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.rag.config.RagCacheKeyBuilder;
import com.ruoyi.rag.domain.MessagesLogs;
import com.ruoyi.rag.entity.ChatMessageEntity;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


public class CustomChatMemory implements ChatMemory {

    private final Long userId; // 当前用户 ID
    private final RedisTemplate<String, Object> redisTemplate; // Redis 操作模板
    private final RagCacheKeyBuilder cacheKeyBuilder;


    public CustomChatMemory(Long userId, RedisTemplate<String, Object> redisTemplate, RagCacheKeyBuilder cacheKeyBuilder) {
        this.userId = userId;
        this.redisTemplate = redisTemplate;
        this.cacheKeyBuilder = cacheKeyBuilder;
    }

    @Override
    public String id() {
        // 返回当前用户的唯一标识
        return cacheKeyBuilder.buildRagChatMemoryKey(userId);
    }

    @Override
    public void add(ChatMessage chatMessage) {
        String redisKey = this.id();
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity(chatMessage.type().toString().toLowerCase(), chatMessage.text());
        // 载入 redis 缓存
        redisTemplate.opsForList().rightPush(redisKey, chatMessageEntity);
    }

    @Override
    public List<ChatMessage> messages() {
        // 从 Redis 中获取当前用户的列表信息
        String redisKey = this.id();
        List<Object> range = redisTemplate.opsForList().range(redisKey, 0, -1);
        // redis 取出来的是 List<Object> ，序列化成 List<ChatMessageEntity>
        List<ChatMessageEntity> chatMessageEntities = JSONArray.parseArray(JSONArray.toJSONString(range), ChatMessageEntity.class);

        // 根据 ChatMessageEntity 的内容封装 ChatMessage 对象
        List<ChatMessage> collect = chatMessageEntities.stream().map(item -> {
            String type = item.getType();
            if (type.equals("ai")) {
                return new AiMessage(item.getText());
            } else if (type.equals("user")) {
                return new UserMessage(item.getText());
            }
            else {
                return new UserMessage("");
            }
        }).collect(Collectors.toList());
        // TODO 如果Redis中无法获取，就去 MySQL 中拉取，如果都没有就返回空列表
        return collect;
    }

    @Override
    public void clear() {
        // 清除 Redis 中当前用户的消息
        String redisKey = id();
        redisTemplate.delete(redisKey);
    }
}
