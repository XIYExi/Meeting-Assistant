package com.ruoyi.router.service;

import com.ruoyi.common.entity.im.ImMsgBody;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ImRouterService {

    Mono<Boolean> sendMsg(Long objectId, String msgJson);

    boolean sendBatchMsg(List<ImMsgBody> imMsgBodyList);
}
