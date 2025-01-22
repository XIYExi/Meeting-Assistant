package com.ruoyi.cos.controller;

import com.qcloud.cos.COSClient;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.cos.constant.CosType;
import com.ruoyi.cos.service.CosService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping(value = "/cos")
@AllArgsConstructor
public class CosController {

    private COSClient cosClient;

    private CosService cosService;

    @PostMapping("/uploadImage")
    public String uploadImage(
            @RequestParam("img") File img,
            @RequestParam("key") String key,
            @RequestParam("fileName")String fileName){
        String s = cosService.uploadImage(img, key, fileName);
        return s;
    }


    @PostMapping("/upload")
    public AjaxResult uploadFileWithId(
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") String id) {
        cosService.uploadFile(file, id, CosType.AVATAR);
        return AjaxResult.success("success");
    }
}
