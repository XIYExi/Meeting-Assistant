package com.ruoyi.agent.constant;

public interface AgentRequestPath {

    String agentUrl = "https://www.das-ai.com";

    String agentExecuteUrl = "/open/api/v2/agent/execute";

    static String generateRequestPath(String path) {
        return agentUrl + path;
    }

}
