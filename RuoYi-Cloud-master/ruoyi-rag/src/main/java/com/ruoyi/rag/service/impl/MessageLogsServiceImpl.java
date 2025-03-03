package com.ruoyi.rag.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.rag.domain.MessagesLogs;
import com.ruoyi.rag.mapper.MessageLogsMapper;
import com.ruoyi.rag.service.MessageLogsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageLogsServiceImpl extends ServiceImpl<MessageLogsMapper, MessagesLogs> implements MessageLogsService {

    @Resource
    private MessageLogsMapper messageLogsMapper;

}
