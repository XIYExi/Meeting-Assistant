package com.ruoyi.cos.service;

import com.ruoyi.cos.constant.CosType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

public interface CosService {
    boolean uploadFile(MultipartFile file, String id, CosType type);

    boolean removeImage(String url);

    Map<String, Object> uploadClip(MultipartFile file);

    boolean removeClip(String url);
}
