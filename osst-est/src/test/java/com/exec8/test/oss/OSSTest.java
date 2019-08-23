package com.exec8.test.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.internal.OSSUtils;
import com.exec8.alioss.config.OssProperties;
import com.exec8.alioss.util.OssUtils;
import com.exec8.test.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Map;

/**
 * com.exec8.test.oss
 * --简单描述类的作用
 *
 * @author jesson
 * @date 2019/8/23 11:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OSSTest {

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private OssProperties ossProperties;

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    @Test
    public void testOss(){
        OssUtils.upload(bucket,ossClient,"file1.txt",new File("D:\\file1.txt"));
        Map<String, String> sinnature = OssUtils.getSinnature(ossClient, ossProperties, bucket, "opt/");
        System.out.println(sinnature);
    }

}
