package com.ruoyi.rag.handler;

import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.rag.declare.ImMsgBizFactory;
import com.ruoyi.rag.declare.MessageHandler;
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
