package com.ruoyi.router.contoller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.router.service.ImRouterService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/router")
public class RouterController {

    @Resource
    private ImRouterService routerService;

    @GetMapping(value = "/sendMsg")
    public Mono<AjaxResult> sendMsg(@RequestParam("userId") Long userId, @RequestParam("msgJson") String msgJson) {
        return routerService.sendMsg(userId, msgJson)
                .flatMap(result -> {
                    if (result) {
                        return Mono.just(AjaxResult.success()); // 返回成功的 AjaxResult
                    } else {
                        return Mono.just(AjaxResult.error()); // 返回失败的 AjaxResult
                    }
                })
                .onErrorResume(error -> {
                    // 处理异常情况
                    return Mono.just(AjaxResult.error("RPC 调用失败: " + error.getMessage()));
                });
    }

//    @PostMapping(value = "/batchSendMsg")
//    public AjaxResult batchSendMsg(@RequestBody List<ImMsgBody> imMsgBodyList) {
//        boolean b = routerService.sendBatchMsg(imMsgBodyList);
//        return b ? AjaxResult.success() : AjaxResult.error();
//    }

}
