package com.ruoyi.cos.service.impl;

import com.qcloud.cos.COSClient;
import com.ruoyi.cos.config.CosConfig;
import com.ruoyi.cos.constant.CosType;
import com.ruoyi.cos.service.CosService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

@Service
@Slf4j
@AllArgsConstructor
public class CosServiceImpl implements CosService {

    private COSClient cosClient;
    private CosConfig cosConfig;

    @Value("${cos.filepath}")
    private String filepath;


    @Override
    public boolean uploadFile(MultipartFile file, String id, CosType type) {
        return false;
    }

    @Override
    public boolean uploadFile(MultipartFile file) {
        return false;
    }

    @Override
    public boolean deleteFile(String filename) {
        return false;
    }

    @Override
    public String getFileUrl(String filename) {
        return null;
    }

    @Override
    public String uploadImage(File file, String key, String fileName) {
        return null;
    }
}
