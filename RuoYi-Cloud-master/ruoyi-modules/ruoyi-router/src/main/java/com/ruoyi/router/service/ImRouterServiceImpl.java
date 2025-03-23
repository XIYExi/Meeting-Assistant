package com.ruoyi.router.service;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.im.api.RemoteImService;
import com.ruoyi.router.constant.ImCoreServerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Mono<Boolean> sendMsg(Long objectId, String msgJson) {
        ImMsgBody imMsgBody = JSON.parseObject(msgJson, ImMsgBody.class);
        int appId = imMsgBody.getAppId();
        String bindAddress = stringRedisTemplate.opsForValue().get(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + imMsgBody.getUserId());
        if (bindAddress.isEmpty()) {
            return Mono.just(false);
        }
        bindAddress = bindAddress.substring(0, bindAddress.indexOf("%"));
        /**
         * 不同的im server和不同的客户端维持连接，所以需要把消息返回到指定的机器上，不能直接直接feign，这样集群部署下，消息不能发送到指定设备
         * 从redis中获取发送机器的ip地址，然后通过原生resttemplate请求数据
         */
        Mono<AjaxResult> rpc = remoteImService.rpc(msgJson);
        rpc.subscribe(
                ajaxResult -> {
                    // 处理成功结果
                    System.out.println("RPC 调用成功，结果: " + ajaxResult);
                },
                error -> {
                    // 处理错误
                    System.err.println("RPC 调用失败，错误: " + error.getMessage());
                },
                () -> {
                    // 处理完成
                    System.out.println("RPC 调用完成");
                }
        );
        return Mono.just(true);
    }

    @Override
    public boolean sendBatchMsg(List<ImMsgBody> imMsgBodyList) {
        List<Long> userIdList = imMsgBodyList.stream().map(ImMsgBody::getUserId).collect(Collectors.toList());
        int appId = imMsgBodyList.get(0).getAppId();

        Map<Long, ImMsgBody> userIdMsgMap = imMsgBodyList.stream().collect(Collectors.toMap(ImMsgBody::getUserId, x -> x));

        List<String> cacheKeyList = new ArrayList<>();
        userIdList.forEach(userId -> {
            String cacheKey = ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId;
            cacheKeyList.add(cacheKey);
        });
        //批量取出每个用户绑定的ip地址
        List<String> ipList = stringRedisTemplate.opsForValue().multiGet(cacheKeyList).stream().filter(x -> x != null).collect(Collectors.toList());
        Map<String, List<Long>> userIdMap = new HashMap<>();
        ipList.forEach(ip -> {
            String currentIp = ip.substring(0, ip.indexOf("%"));
            Long userId = Long.valueOf(ip.substring(ip.indexOf("%") + 1));
            List<Long> currentUserIdList = userIdMap.get(currentIp);
            if (currentUserIdList == null) {
                currentUserIdList = new ArrayList<>();
            }
            currentUserIdList.add(userId);
            userIdMap.put(currentIp, currentUserIdList);
        });


        for (String currentIp : userIdMap.keySet()) {
            // RpcContext.getContext().set("ip", currentIp);
            List<ImMsgBody> batchSendMsgGroupByIpList = new ArrayList<>();
            List<Long> ipBindUserIdList = userIdMap.get(currentIp);
            for (Long userId : ipBindUserIdList) {
                ImMsgBody imMsgBody = userIdMsgMap.get(userId);
                batchSendMsgGroupByIpList.add(imMsgBody);
            }
            // routerHandlerRpc.batchSendMsg(batchSendMsgGroupByIpList);
            remoteImService.batchRpc(batchSendMsgGroupByIpList);
        }
        return true;
    }
}
