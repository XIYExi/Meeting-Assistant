package com.ruoyi.rag.declare;

import com.ruoyi.rag.domain.StepSplitEntity;

import java.util.List;

/**
 *
 */
public interface ToolDispatchFactory {

    /**
     * v1 版本
     * 优点：速度快，简单，稳定
     * 缺点：只能识别单条命令，可以检查查询数据库
     * @param type
     * @param content
     * @return
     */
    String dispatch(String type, String content);

    /**
     * v2 版本
     * 优点：可以识别组合命令，执行复杂任务，操作嵌入软件
     * 缺点：运行速度慢
     * @param step
     * @return
     */
    String dispatch(List<StepSplitEntity> step);

}
