package com.ruoyi.cos.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.cos.service.CosService;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private CosService cosService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileAvatar(MultipartFile[] files) {
        ArrayList<Map<String, Object>> urls = new ArrayList<>();
        for (int i = 0; i < files.length; ++i) {
            Map<String, Object> map = cosService.uploadClip(files[i]);
            urls.add(map);
        }
        return AjaxResult.success(urls);
    }

    @PostMapping(value = "/removeClip")
    public AjaxResult removeClip(@RequestParam("url") String url) {
        boolean b = cosService.removeClip(url);
        return b ? AjaxResult.success() : AjaxResult.error();
    }

}
