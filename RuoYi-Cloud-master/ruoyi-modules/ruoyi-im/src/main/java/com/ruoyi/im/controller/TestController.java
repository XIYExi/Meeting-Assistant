package com.ruoyi.im.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/info")
    public AjaxResult info() {
        String token = SecurityUtils.getToken();
        LoginUser loginUser = SecurityUtils.getLoginUser();

        log.info("token: {}", token);
        log.info("loginUser: {}", loginUser);
        return AjaxResult.success();
    }



}
