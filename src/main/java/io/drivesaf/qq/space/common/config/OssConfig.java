package io.drivesaf.qq.space.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: DRIVESAF
 * @createTime: 2024/04/28 19:29
 * @description:
 **/
@Configuration
@ConfigurationProperties(prefix="aliyun.oss")
@Data
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
