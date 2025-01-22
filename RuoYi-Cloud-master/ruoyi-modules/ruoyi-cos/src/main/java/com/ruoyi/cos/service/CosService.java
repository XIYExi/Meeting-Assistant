package com.ruoyi.cos.service;

import com.ruoyi.cos.constant.CosType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CosService {
    boolean uploadFile(MultipartFile file, String id, CosType type);

    boolean removeImage(String url);
}
