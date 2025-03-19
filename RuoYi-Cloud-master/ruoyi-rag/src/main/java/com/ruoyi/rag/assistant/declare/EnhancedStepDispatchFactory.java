package com.ruoyi.rag.assistant.declare;

import com.ruoyi.rag.assistant.entity.StepDefinition;

import java.util.List;

/**
 *
 */
public interface EnhancedStepDispatchFactory {

    /**
     * v2 版本
     * 优点：可以识别组合命令，执行复杂任务，操作嵌入软件
     * 缺点：运行速度慢
     * @param steps
     * @param uid 会话id，用来ws发消息回去
     * @return
     */
    String dispatch(List<StepDefinition> steps, String uid);

}
