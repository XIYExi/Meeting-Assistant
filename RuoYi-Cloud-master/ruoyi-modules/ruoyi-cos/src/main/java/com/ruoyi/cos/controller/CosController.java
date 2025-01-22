package com.ruoyi.cos.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.cos.constant.CosType;
import com.ruoyi.cos.service.CosService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/cos")
@AllArgsConstructor
public class CosController {

    private CosService cosService;


    @PostMapping("/removeImage")
    public AjaxResult removeImage(@RequestParam("url") String url) {
        boolean b = cosService.removeImage(url);
        return b ? AjaxResult.success() : AjaxResult.error();
    }



    @PostMapping(value = "/uploadAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileAvatar(
            @RequestPart("file") MultipartFile file,
            @RequestPart("id") String id) {
        cosService.uploadFile(file, id, CosType.AVATAR);
        return AjaxResult.success("success");
    }


    @PostMapping(value = "/uploadSystem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileSystem(
            @RequestPart("file") MultipartFile file,
            @RequestPart("id") String id) {
        cosService.uploadFile(file, id, CosType.SYSTEM_IMAGE);
        return AjaxResult.success("success");
    }

    @PostMapping(value = "/uploadCommon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileCommon(
            @RequestPart("file") MultipartFile file,
            @RequestPart("id") String id) {
        cosService.uploadFile(file, id, CosType.COMMON_IMAGE);
        return AjaxResult.success("success");
    }

    @PostMapping(value = "/uploadSocial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileSocial(
            @RequestPart("file") MultipartFile file,
            @RequestPart("id") String id) {
        cosService.uploadFile(file, id, CosType.SOCIAL_IMAGE);
        return AjaxResult.success("success");
    }



}
