package com.ruoyi.common.encrypt.properties;

import com.ruoyi.common.encrypt.enumd.AlgorithmType;
import com.ruoyi.common.encrypt.enumd.EncodeType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 加解密属性配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mybatis-encryptor")
public class EncryptorProperties {

    /**
     * 过滤开关
     */
    private Boolean enable;

    /**
     * 默认算法
     */
    private AlgorithmType algorithm;

    /**
     * 安全秘钥
     */
    private String password;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 编码方式，base64/hex
     */
    private EncodeType encode;

}
