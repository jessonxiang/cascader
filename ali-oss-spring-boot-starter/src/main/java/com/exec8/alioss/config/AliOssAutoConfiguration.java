package com.exec8.alioss.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * com.exec8.alioss.config
 * --简单描述类的作用
 *
 * @author jesson
 * @date 2019-08-22 17:51
 */
@Configuration
@ConditionalOnProperty(prefix = "aliyun.oss",name = {"endpoint","accessKeyId","accessKeySecret"})
@EnableConfigurationProperties(OssProperties.class)
public class AliOssAutoConfiguration {

    private OssProperties ossProperties;

    public AliOssAutoConfiguration(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @Bean
    public OSSClient ossClientFactoryBean() {
        String endpoint = this.ossProperties.getEndpoint();
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(this.ossProperties.getAccessKeyId(), this.ossProperties.getAccessKeySecret());
        ClientConfiguration clientConfiguration = new ClientBuilderConfiguration();
        final OSSClient ossClient = new OSSClient(endpoint, credentialsProvider, clientConfiguration);
        return ossClient;
    }
}
