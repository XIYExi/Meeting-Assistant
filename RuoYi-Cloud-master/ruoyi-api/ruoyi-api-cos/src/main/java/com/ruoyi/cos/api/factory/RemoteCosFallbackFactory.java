package com.ruoyi.cos.api.factory;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.cos.api.RemoteCosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务降级处理
 * 
 * @author ruoyi
 */
@Component
public class RemoteCosFallbackFactory implements FallbackFactory<RemoteCosService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteCosFallbackFactory.class);

    @Override
    public RemoteCosService create(Throwable throwable)
    {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteCosService()
        {
            @Override
            public AjaxResult uploadFileAvatar(MultipartFile file, String id) {
               return AjaxResult.error("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult uploadFileSystem(MultipartFile file, String id) {
               return AjaxResult.error("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult uploadFileCommon(MultipartFile file, String id) {
                return AjaxResult.error("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult uploadFileSocial(MultipartFile file, String id) {
                return AjaxResult.error("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult removeImage(String url) {
                return AjaxResult.error("文件删除失败:" + throwable.getMessage());
            }
        };
    }
}
