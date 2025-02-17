package com.ruoyi.im.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.im.rpc.IRouterHandlerRpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/im")
public class ImController {

    @Resource
    private IRouterHandlerRpc routerHandlerRpc;

    @GetMapping("/rpc")
    public AjaxResult rpc(@RequestParam("userId") Long userId,
                          @RequestParam("msgJson") String msgJson) {
        routerHandlerRpc.sendMsg(userId, msgJson);
        return AjaxResult.success();
    }

}
