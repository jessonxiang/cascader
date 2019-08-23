package com.exec8.alioss.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * com.exec8.alioss.config
 * --简单描述类的作用
 *
 * @author jesson
 * @date 2019-08-22 17:53
 */
@Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
    /**
     * 文件上传的项目
     */
    private String bucket;
    /**
     * 文件上传的域名不包括 bucket名字
     */
    private String endpoint;
    /**
     * 阿里云的key
     */
    private String accessKeyId;
    /**
     * 阿里云的秘钥
     */
    private String accessKeySecret;
}
