package com.ruoyi.agent.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.agent.mapper.MessagesLogsMapper;
import com.ruoyi.agent.domain.MessagesLogs;
import com.ruoyi.agent.service.IMessagesLogsService;

/**
 * agent聊天记录Service业务层处理
 * 
 * @author xiye
 * @date 2025-01-23
 */
@Service
public class MessagesLogsServiceImpl implements IMessagesLogsService 
{
    @Autowired
    private MessagesLogsMapper messagesLogsMapper;

    /**
     * 查询agent聊天记录
     * 
     * @param id agent聊天记录主键
     * @return agent聊天记录
     */
    @Override
    public MessagesLogs selectMessagesLogsById(Long id)
    {
        return messagesLogsMapper.selectMessagesLogsById(id);
    }

    /**
     * 查询agent聊天记录列表
     * 
     * @param messagesLogs agent聊天记录
     * @return agent聊天记录
     */
    @Override
    public List<MessagesLogs> selectMessagesLogsList(MessagesLogs messagesLogs)
    {
        return messagesLogsMapper.selectMessagesLogsList(messagesLogs);
    }

    /**
     * 新增agent聊天记录
     * 
     * @param messagesLogs agent聊天记录
     * @return 结果
     */
    @Override
    public int insertMessagesLogs(MessagesLogs messagesLogs)
    {
        messagesLogs.setCreateTime(DateUtils.getNowDate());
        return messagesLogsMapper.insertMessagesLogs(messagesLogs);
    }

    /**
     * 修改agent聊天记录
     * 
     * @param messagesLogs agent聊天记录
     * @return 结果
     */
    @Override
    public int updateMessagesLogs(MessagesLogs messagesLogs)
    {
        messagesLogs.setUpdateTime(DateUtils.getNowDate());
        return messagesLogsMapper.updateMessagesLogs(messagesLogs);
    }

    /**
     * 批量删除agent聊天记录
     * 
     * @param ids 需要删除的agent聊天记录主键
     * @return 结果
     */
    @Override
    public int deleteMessagesLogsByIds(Long[] ids)
    {
        return messagesLogsMapper.deleteMessagesLogsByIds(ids);
    }

    /**
     * 删除agent聊天记录信息
     * 
     * @param id agent聊天记录主键
     * @return 结果
     */
    @Override
    public int deleteMessagesLogsById(Long id)
    {
        return messagesLogsMapper.deleteMessagesLogsById(id);
    }
}
