package com.ruoyi.rag.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface Assistant {
    @UserMessage("{{content}}") // 默认角色为 user
    String chat(@V("content") String content);

}
