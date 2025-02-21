package com.ruoyi.live.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.live.domain.MeetingChat;
import com.ruoyi.live.mapper.MessageChatMapper;
import com.ruoyi.live.service.MessageChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageChatServiceImpl extends ServiceImpl<MessageChatMapper, MeetingChat> implements MessageChatService {

    @Resource
    private MessageChatMapper messageChatMapper;

}
