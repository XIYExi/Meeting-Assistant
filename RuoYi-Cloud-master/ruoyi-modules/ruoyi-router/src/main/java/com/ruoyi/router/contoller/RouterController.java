package com.ruoyi.router.contoller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.router.rpc.ImRouterRpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/router")
public class RouterController {

    @Resource
    private ImRouterRpc imRouterRpc;

    @GetMapping(value = "/sendMsg")
    public AjaxResult sendMsg(@RequestParam("userId") Long userId, @RequestParam("msgJson") String msgJson){
        boolean b = imRouterRpc.sendMsg(userId, msgJson);
        return b ? AjaxResult.success() : AjaxResult.error();
    }

}
