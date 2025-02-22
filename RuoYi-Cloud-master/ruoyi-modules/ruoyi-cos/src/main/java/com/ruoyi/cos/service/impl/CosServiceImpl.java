package com.ruoyi.cos.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.DeleteObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.config.CosConfig;
import com.ruoyi.cos.constant.CosType;
import com.ruoyi.cos.domain.Image;
import com.ruoyi.cos.mapper.ImageMapper;
import com.ruoyi.cos.service.CosService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CosServiceImpl implements CosService {

    private COSClient cosClient;
    private CosConfig cosConfig;
    private ImageMapper imageMapper;

    private static final String SOCIAL_IMAGE_PREFIX = "article/";
    private static final String AVATAR_PREFIX = "avatar/";
    private static final String SYSTEM_PREFIX  = "system/";
    private static final String COMMON_PREFIX  = "common/";
    private static final String FILE_PREFIX = "file/";


    @Override
    public boolean uploadFile(MultipartFile file, String id, CosType type) {
        String key = "";
        String filename = file.getOriginalFilename();
        String extend = filename.substring(filename.lastIndexOf(".") + 1);
        switch (type) {
            case AVATAR:
                // avatar/{id}
                key += CosServiceImpl.AVATAR_PREFIX + id + "." + extend;
                break;
            case SOCIAL_IMAGE:
                // article/{id}
                key += CosServiceImpl.SOCIAL_IMAGE_PREFIX + id + "." + extend;
                break;
            case SYSTEM_IMAGE:
                // system/{id}
                key += CosServiceImpl.SYSTEM_PREFIX + id + "." + extend;
                break;
            case COMMON_IMAGE:
                key += CosServiceImpl.COMMON_PREFIX + id + "." + extend;
                break;
            default:
                break;
        }

        try {
            // jn-1306384632
            String bucketName = cosConfig.getBucketName();
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, null);
            cosClient.putObject(putObjectRequest);

            // 再插入到数据库中保存图片路径
            Image image = Image.builder()
                    .id(id)
                    .url("https://jn-1306384632.cos.ap-nanjing.myqcloud.com/" + key)
                    .type(type.name())
                    .extend(extend)
                    .build();
            image.setCreateTime(DateUtils.getNowDate());
            imageMapper.insertImage(image);
        } catch (CosClientException e) {
            throw new RuntimeException("文件上传失败");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean removeImage(String url) {
        String[] split = url.split("/");
        String imageFileName = split[split.length - 1];
        String prefix = split[split.length - 2];
        // image命名格式为 {imageId}.{图片格式}
        String imageId = imageFileName.split("\\.")[0];
        String key = prefix + "/" + imageFileName;

        cosClient.deleteObject(new DeleteObjectRequest(cosConfig.getBucketName(), key));
        int i = imageMapper.deleteImageById(imageId);
        return i == 1;
    }

    @Override
    public boolean removeClip(String url) {
        String[] split = url.split("/");
        String imageFileName = split[split.length - 1];
        String prefix = split[split.length - 2];
        String key = prefix + "/" + imageFileName;
        cosClient.deleteObject(new DeleteObjectRequest(cosConfig.getBucketName(), key));
        return true;
    }


    @Override
    public Map<String, Object> uploadClip(MultipartFile file) {
        String key = "";
        String filename = file.getOriginalFilename();
        String extend = filename.substring(filename.lastIndexOf(".") + 1);
        key += CosServiceImpl.FILE_PREFIX + filename;

        try {
            String bucketName = cosConfig.getBucketName();
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, null);
            cosClient.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败");
        }
        Map<String, Object> map = new HashMap<>();

        String url = "https://jn-1306384632.cos.ap-nanjing.myqcloud.com/" + CosServiceImpl.FILE_PREFIX + filename;
        map.put("url", url);
        map.put("fileName", filename);
        map.put("fileType", extend);
        map.put("fileSize", file.getSize());

        return map;
    }


}
