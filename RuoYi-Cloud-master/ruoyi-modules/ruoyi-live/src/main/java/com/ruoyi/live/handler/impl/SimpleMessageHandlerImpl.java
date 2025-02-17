package com.ruoyi.live.handler.impl;

import com.ruoyi.live.entity.ImMsgBody;
import com.ruoyi.live.enums.ImMsgBizCodeEnum;
import com.ruoyi.live.handler.ImMsgBizFactory;
import com.ruoyi.live.handler.MessageHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SimpleMessageHandlerImpl implements MessageHandler {

    @Resource
    private ImMsgBizFactory imMsgBizFactory;

    @Override
    public void onMsgReceiver(ImMsgBody imMsgBody) {
        // 直接使用工厂函数做分发，在对应的handler里面处理ImMsgBody
        imMsgBizFactory.doMsgHandler(imMsgBody);
    }
}
