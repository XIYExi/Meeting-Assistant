package com.ruoyi.fisco.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {


    @GetMapping(value = "/read")
    public AjaxResult read() {
        return AjaxResult.success();
    }

}
