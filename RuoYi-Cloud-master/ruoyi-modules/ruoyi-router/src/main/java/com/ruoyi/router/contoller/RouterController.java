package com.ruoyi.router.contoller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.router.service.ImRouterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/router")
public class RouterController {

    @Resource
    private ImRouterService routerService;

    @GetMapping(value = "/sendMsg")
    public AjaxResult sendMsg(@RequestParam("userId") Long userId, @RequestParam("msgJson") String msgJson){
        boolean b = routerService.sendMsg(userId, msgJson);
        return b ? AjaxResult.success() : AjaxResult.error();
    }

    @PostMapping(value = "/batchSendMsg")
    public AjaxResult batchSendMsg(@RequestBody List<ImMsgBody> imMsgBodyList){
        boolean b = routerService.sendBatchMsg(imMsgBodyList);
        return b ? AjaxResult.success() : AjaxResult.error();
    }

}
