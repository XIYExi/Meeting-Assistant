package com.ruoyi.cos.api;


import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.cos.api.factory.RemoteCosFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * cos腾讯云桶存储服务
 */
@FeignClient(contextId = "remoteCosService", value = ServiceNameConstants.COS_SERVICE, fallbackFactory = RemoteCosFallbackFactory.class)
public interface RemoteCosService {

    @PostMapping(value = "/cos/uploadAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileAvatar(@RequestPart("file") MultipartFile file, @RequestPart("id") String id);

    @PostMapping(value = "/cos/uploadSystem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileSystem(@RequestPart("file") MultipartFile file, @RequestPart("id") String id);

    @PostMapping(value = "/cos/uploadCommon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileCommon(@RequestPart("file") MultipartFile file, @RequestPart("id") String id);

    @PostMapping(value = "/cos/uploadSocial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadFileSocial(@RequestPart("file") MultipartFile file, @RequestPart("id") String id);

    @PostMapping(value = "/cos/removeImage")
    public AjaxResult removeImage(@RequestParam("url") String url);
}
