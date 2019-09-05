package com.exec8.alioss.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.exec8.alioss.config.OssProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * com.exec8.alioss.config
 * --简单描述类的作用
 *
 * @author jesson
 * @date 2019/8/23 10:38
 */
public class OssUtils {

    private static final Logger LOG = LoggerFactory.getLogger(OssUtils.class);

    /**
     * getSinnature:(获取oss上传签名参数). <br/>
     *
     * @param ossClient
     * @param ossProperties oss配置信息
     * @param bucket        上传的项目 bucket
     * @param dir           文件上传的路径
     * @return
     * @author jesson.xiang@blackhills.com.cn
     * @since
     */
    public static Map<String, String> getSinnature(OSSClient ossClient, OssProperties ossProperties, String bucket, String dir) {
        String host = "https://" + bucket + "." + ossProperties.getEndpoint();
        try {
            /**
             * 10分钟的过期时间
             */
            long expireTime = 10 * 60;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", ossProperties.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("host", host);
            respMap.put("dir", dir);
            respMap.put("expiryTime", expireEndTime + "");
            LOG.info("accessid:" + ossProperties.getAccessKeyId() + ",policy" + encodedPolicy + ",signature" + postSignature + ",host" + host);
            return respMap;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * getUrl:(生成访问URL). <br/>
     *
     * @param ossClient 自动初始化对象
     * @param bucket    上传的项目 bucket
     * @param key       文件路径全称
     * @return
     * @author jesson.xiang@blackhills.com.cn
     * @since
     */
    public static String getUrl(OSSClient ossClient, String bucket, String key) {
        try {
            /**
             * 30秒的过期时间
             */
            Date expiration = new Date(System.currentTimeMillis() + 30 * 1000);
            // 生成URL
            URL url = ossClient.generatePresignedUrl(bucket, key, expiration);
            return url.toString();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public static void synchronizeFile(String basepath, String bucket, OSSClient ossClient) {
        File file = new File(basepath);
        LOG.error("同步文件开始");
        saveOss(file, bucket, ossClient);
        LOG.error("同步文件完成");
    }

    private static void saveOss(File file, String bucket, OSSClient ossClient) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    saveOss(file2, bucket, ossClient);
                } else {
                    saveOssFile(file2, bucket, ossClient);
                }
            }
        } else {
            saveOssFile(file, bucket, ossClient);
        }
    }

    private static void saveOssFile(File file, String bucket, OSSClient ossClient) {
//		 上传文件
        String key = file.getPath().replaceAll("\\\\", "/");
        LOG.info("同步文件:" + key + "==" + file.getName());
        if (key.substring(0, 1).equals("/")) {
            key = key.substring(1);
        }
        ossClient.putObject(bucket, key, file);
    }

    /**
     * downLoadFile:(下载oss文件). <br/>
     *
     * @param file       下载到哪里的文件
     * @param key        文件的路径
     * @param ossClient  自动注入的client对象
     * @param bucketName 名字
     * @author jesson.xiang@blackhills.com.cn
     * @since
     */
    public static void downLoadFile(File file, String key, OSSClient ossClient, String bucketName) {
        // 下载object到文件
        ossClient.getObject(new GetObjectRequest(bucketName, key), file);
    }

    public static OSSObject downLoadObject(String key, OSSClient ossClient, String bucketName) {
        // 下载object到文件
        OSSObject object = ossClient.getObject(new GetObjectRequest(bucketName, key));
        return object;
    }

    public static OSSClient getOSSClient(String endpoint, String accessKeyId, String accessKeySecret) {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }


    public static OSSObject getOSSObject(OSSClient client, String bucketName, String key) {
        return client.getObject(new GetObjectRequest(bucketName, key));
    }


    public static void shutdownOSSClient(OSSClient client) {
        client.shutdown();
    }


    /**
     * downLoadFile:(上传oss文件). <br/>
     *
     * @param file       上传的的文件
     * @param key        文件的路径
     * @param ossClient  ossClient
     * @param bucketName 名字
     * @author jesson.xiang@blackhills.com.cn
     * @since
     */
    public static void uploadFile(File file, String key, OSSClient ossClient, String bucketName) {
        // 上传文件
        ossClient.putObject(bucketName, key, file);
    }


    public static PutObjectResult upload(String bucketName, OSSClient ossClient, String key, File file) {
        if (key.substring(0, 1).equals("/")) {
            key = key.substring(1);
        }
        // 上传文件流
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, key, file);
        return putObjectResult;
    }

}
