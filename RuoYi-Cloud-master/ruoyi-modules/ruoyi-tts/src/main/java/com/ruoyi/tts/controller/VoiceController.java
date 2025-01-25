package com.ruoyi.tts.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/voice")
public class VoiceController {


    @GetMapping("/send")
    public AjaxResult send() {

        return AjaxResult.success();
    }

}
