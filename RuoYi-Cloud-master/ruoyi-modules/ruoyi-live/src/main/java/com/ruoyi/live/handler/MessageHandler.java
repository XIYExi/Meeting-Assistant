package com.ruoyi.live.handler;


import com.ruoyi.common.entity.im.ImMsgBody;

public interface MessageHandler {

    void onMsgReceiver(ImMsgBody imMsgBody);
}
