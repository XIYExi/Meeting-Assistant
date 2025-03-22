package com.ruoyi.rag.declare;


import com.ruoyi.common.entity.im.ImMsgBody;

public interface MessageHandler {

    void onMsgReceiver(ImMsgBody imMsgBody);
}
