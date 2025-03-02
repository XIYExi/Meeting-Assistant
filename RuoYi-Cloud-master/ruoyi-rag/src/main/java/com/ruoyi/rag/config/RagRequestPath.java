package com.ruoyi.rag.config;

public interface RagRequestPath {
    String agentUrl = "https://www.das-ai.com";

    String ragExecuteUrl = "/open/api/v1/chat";

    String agentExecuteUrl = "/open/api/v2/agent/execute";

    static String generateRequestPath(String path) {
        return agentUrl + path;
    }
}
