package com.ruoyi.im.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.im.entity.ImConfigVO;
import com.ruoyi.im.rpc.IRouterHandlerRpc;
import com.ruoyi.im.service.ImService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/im")
public class ImController {

    @Resource
    private ImService imService;
    @Resource
    private IRouterHandlerRpc routerHandlerRpc;

    /**
     * 流式返回
     * @param msgJson
     * @return
     */
    @GetMapping("/rpc")
    public Mono<AjaxResult> rpc(@RequestParam("msgJson") String msgJson) {
        System.err.println("!!");
        return routerHandlerRpc.sendMsg(msgJson).thenReturn(AjaxResult.success());
    }

    @PostMapping("/batchRpc")
    public AjaxResult batchRpc(@RequestBody List<ImMsgBody> imMsgBodyList) {
        System.err.println("??");
        imMsgBodyList.forEach(imMsgBody -> {
            routerHandlerRpc.sendMsg(JSON.toJSONString(imMsgBody));
        });
       return AjaxResult.success();
    }

    @GetMapping("/imConfig")
    public AjaxResult getImConfig() {
        ImConfigVO imConfig = imService.getImConfig();
        return AjaxResult.success(imConfig);
    }

}
