package com.ruoyi.rag.service;

import com.ruoyi.common.entity.im.ImOfflineDTO;
import com.ruoyi.common.entity.im.ImOnlineDTO;

import java.util.List;

public interface AgentRoomService{

    void userOnlineHandler(ImOnlineDTO imOnlineDTO);

    void userOfflineHandler(ImOfflineDTO imOfflineDTO);

}
