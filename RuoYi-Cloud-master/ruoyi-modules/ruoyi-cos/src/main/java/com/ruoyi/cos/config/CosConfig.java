package com.ruoyi.cos.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CosConfig implements InitializingBean {
    @Value("${cos.secretId}")
    private String secretId;
    @Value("${cos.secretKey}")
    private String secretKey;
    @Value("${cos.region}")
    private String region;
    @Value("${cos.bucketName}")
    private String bucketName;
    @Value("${cos.durationSeconds}")
    private String durationSeconds;
    @Value("${cos.url}")
    private String url;

    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String BUCKET_NAME;
    public static String REGION;
    public static String URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        BUCKET_NAME = bucketName;
        REGION = region;
        URL = url;
    }

    @Bean
    public COSClient cosClient() {
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        Region region = new Region(REGION);
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }
}
