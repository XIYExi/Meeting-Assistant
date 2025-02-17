package com.ruoyi.live.handler;

import com.ruoyi.live.entity.ImMsgBody;

public interface MessageHandler {

    void onMsgReceiver(ImMsgBody imMsgBody);
}
