package com.ruoyi.router.service;

import com.ruoyi.common.entity.im.ImMsgBody;

import java.util.List;

public interface ImRouterService {

    boolean sendMsg(Long objectId, String msgJson);

    boolean sendBatchMsg(List<ImMsgBody> imMsgBodyList);
}
