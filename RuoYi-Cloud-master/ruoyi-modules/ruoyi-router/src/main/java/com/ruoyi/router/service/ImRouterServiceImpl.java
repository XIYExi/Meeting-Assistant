package com.ruoyi.router.service;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.im.api.RemoteImService;
import com.ruoyi.router.constant.ImCoreServerConstants;
import com.ruoyi.router.entity.ImMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class ImRouterServiceImpl implements ImRouterService {

    private Logger logger = LoggerFactory.getLogger(ImRouterServiceImpl.class);

    @Resource
    private RemoteImService remoteImService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RestTemplate restTemplate;

    @Override
    public boolean sendMsg(Long objectId, String msgJson) {
        ImMsgBody imMsgBody = JSON.parseObject(msgJson, ImMsgBody.class);
        int appId = imMsgBody.getAppId();
        String bindAddress = stringRedisTemplate.opsForValue().get(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":"  + objectId);
        if (bindAddress.isEmpty()) {
            return false;
        }
        /**
         * 不同的im server和不同的客户端维持连接，所以需要把消息返回到指定的机器上，不能直接直接feign，这样集群部署下，消息不能发送到指定设备
         * 从redis中获取发送机器的ip地址，然后通过原生resttemplate请求数据
         */
        AjaxResult rpc = remoteImService.rpc(objectId, msgJson);
        System.err.println(msgJson);
//        ResponseEntity<AjaxResult> forEntity = restTemplate.getForEntity(
//                "http://" + bindAddress + "/im/rpc?userId=" + objectId + "&msgJson=" + msgJson,
//                AjaxResult.class
//        );
        logger.info("rpc result {}", rpc);
        return true;
    }
}
