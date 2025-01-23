package com.ruoyi.agent.mapper;

import java.util.List;
import com.ruoyi.agent.domain.MessagesLogs;
import org.apache.ibatis.annotations.Mapper;

/**
 * agent聊天记录Mapper接口
 * 
 * @author xiye
 * @date 2025-01-23
 */
@Mapper
public interface MessagesLogsMapper 
{
    /**
     * 查询agent聊天记录
     * 
     * @param id agent聊天记录主键
     * @return agent聊天记录
     */
    public MessagesLogs selectMessagesLogsById(Long id);

    /**
     * 查询agent聊天记录列表
     * 
     * @param messagesLogs agent聊天记录
     * @return agent聊天记录集合
     */
    public List<MessagesLogs> selectMessagesLogsList(MessagesLogs messagesLogs);

    /**
     * 新增agent聊天记录
     * 
     * @param messagesLogs agent聊天记录
     * @return 结果
     */
    public int insertMessagesLogs(MessagesLogs messagesLogs);

    /**
     * 修改agent聊天记录
     * 
     * @param messagesLogs agent聊天记录
     * @return 结果
     */
    public int updateMessagesLogs(MessagesLogs messagesLogs);

    /**
     * 删除agent聊天记录
     * 
     * @param id agent聊天记录主键
     * @return 结果
     */
    public int deleteMessagesLogsById(Long id);

    /**
     * 批量删除agent聊天记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMessagesLogsByIds(Long[] ids);
}
