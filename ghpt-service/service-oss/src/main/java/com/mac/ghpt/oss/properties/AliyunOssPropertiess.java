package com.mac.ghpt.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年05月29日, 20:29:12
 */
@Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssPropertiess {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}