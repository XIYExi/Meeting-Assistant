package com.ruoyi.live.handler.impl;

import com.ruoyi.live.entity.ImMsgBody;
import com.ruoyi.live.enums.ImMsgBizCodeEnum;
import com.ruoyi.live.handler.ImMsgBizFactory;
import com.ruoyi.live.handler.SimpleMsgHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class ImMsgBizFactoryImpl implements ImMsgBizFactory, InitializingBean {

    @Resource
    ApplicationContext applicationContext;

    private static final Map<Integer, SimpleMsgHandler> simpleHandlerMap = new HashMap<>();

    @Override
    public void doMsgHandler(ImMsgBody imMsgBody) {
        SimpleMsgHandler simpleMsgHandler = simpleHandlerMap.get(imMsgBody.getBizCode());
        if (simpleMsgHandler == null) {
            throw new IllegalArgumentException("biz code is error, code is : " + imMsgBody.getBizCode());
        }
        simpleMsgHandler.handler(imMsgBody);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        simpleHandlerMap.put(ImMsgBizCodeEnum.LIVE_ROOM_IM_BIZ.getCode(), applicationContext.getBean(LiveRoomMsgHandler.class));
    }
}
