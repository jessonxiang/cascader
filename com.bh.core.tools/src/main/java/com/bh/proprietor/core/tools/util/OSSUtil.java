package com.bh.proprietor.core.tools.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by chandler on 2017/9/29 0029.
 */
public class OSSUtil {
    private static final Logger LOG = LoggerFactory.getLogger(OSSUtil.class);
    /**
     *
     * getSinnature:(获取oss上传签名参数). <br/>
     *
     * @author jesson.xiang@blackhills.com.cn
     * @param endpoint 文件上传的域名不包括 bucket名字
     * @param accessId accessid必须在阿里云申请
     * @param accessKey accesskey 阿里云申请的key
     * @param bucket   上传的项目 bucket
     * @param dir   文件上传的路径
     * @return
     * @since
     */
    public static Map<String, String> getSinnature(String endpoint, String accessId, String accessKey, String bucket, String dir){
        String host = "http://" + bucket + "." + endpoint;
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            /**
             * 10分钟的过期时间
             */
            long expireTime = 10*60;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("host", host);
            respMap.put("dir", dir);
            LOG.info("accessid:"+accessId+",policy"+encodedPolicy+",signature"+postSignature+",host"+host);
            return respMap;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     *
     * getUrl:(生成访问URL). <br/>
     * @author jesson.xiang@blackhills.com.cn
     * @param endpoint 文件上传的域名不包括 bucket名字
     * @param accessId accessid必须在阿里云申请
     * @param accessKey accesskey 阿里云申请的key
     * @param bucket   上传的项目 bucket
     * @param key 文件路径全称
     * @return
     * @since
     */
    public static String getUrl(String endpoint,String accessId,String accessKey,String bucket,String key){
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            /**
             * 30秒的过期时间
             */
            Date expiration = new Date(System.currentTimeMillis() + 30 * 1000);
            // 生成URL
            URL url = client.generatePresignedUrl(bucket, key, expiration);
            return url.toString();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public static void synchronizeFile(String basepath,String bucket,String endpoint,String accessKeyId,String accessKeySecret) {
        File file = new File(basepath);
        LOG.error("同步文件开始");
        saveOss(file,bucket,endpoint,accessKeyId,accessKeySecret);
        LOG.error("同步文件完成");
    }

    private static void saveOss(File file,String bucket,String endpoint,String accessKeyId,String accessKeySecret){
        if(file.isDirectory()){
            File[] listFiles = file.listFiles();
            for (File file2 : listFiles) {
                if(file2.isDirectory()){
                    saveOss(file2,bucket,endpoint,accessKeyId,accessKeySecret);
                }else{
                    saveOssFile(file2,bucket,endpoint,accessKeyId,accessKeySecret);
                }
            }
        }else{
            saveOssFile(file,bucket,endpoint,accessKeyId,accessKeySecret);
        }
    }

    private static void saveOssFile(File file,String bucket,String endpoint,String accessKeyId,String accessKeySecret){
//		 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//		 上传文件
        String key = file.getPath().replaceAll("\\\\" , "/");
        LOG.info("同步文件:"+key+"=="+file.getName());
        if(key.substring(0, 1).equals("/")){
            key = key.substring(1);
        }
        ossClient.putObject(bucket, key,file);
//		 关闭client
        ossClient.shutdown();
    }
    /**
     *
     * downLoadFile:(下载oss文件). <br/>
     * @author jesson.xiang@blackhills.com.cn
     * @param file 下载到哪里的文件
     * @param key 文件的路径
     * @param endpoint 访问路径
     * @param accessKeyId key
     * @param accessKeySecret secret
     * @param bucketName 名字
     * @since
     */
    public static void downLoadFile(File file,String key,String endpoint,String accessKeyId,String accessKeySecret,String bucketName){
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 下载object到文件
        ossClient.getObject(new GetObjectRequest(bucketName, key), file);
        // 关闭client
        ossClient.shutdown();
    }

    public static OSSObject downLoadObject(String key,String endpoint,String accessKeyId,String accessKeySecret,String bucketName){
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 下载object到文件
        OSSObject object = ossClient.getObject(new GetObjectRequest(bucketName, key));
        // 关闭client
        ossClient.shutdown();
        return object;
    }

    public static OSSClient getOSSClient(String endpoint,String accessKeyId,String accessKeySecret){
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public static OSSObject getOSSObject(OSSClient client,String bucketName,String key){
        return client.getObject(new GetObjectRequest(bucketName,key));
    }

    public static void shutdownOSSClient(OSSClient client){
        client.shutdown();
    }


    /**
     *
     * downLoadFile:(上传oss文件). <br/>
     * @author jesson.xiang@blackhills.com.cn
     * @param file 上传的的文件
     * @param key 文件的路径
     * @param endpoint 访问路径
     * @param accessKeyId key
     * @param accessKeySecret secret
     * @param bucketName 名字
     * @since
     */
    public static void uploadFile(File file,String key,String endpoint,String accessKeyId,String accessKeySecret,String bucketName){
        // endpoint以杭州为例，其它region请按实际情况填写
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件
        ossClient.putObject(bucketName,key, file);
        // 关闭client
        ossClient.shutdown();
    }


    public static PutObjectResult upload(String bucketName, String point, String accessKeyId, String accessKeySecret, String key, File file){
        if(key.substring(0,1).equals("/")){
            key = key.substring(1);
        }
        // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = "http://"+point;
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流
        PutObjectResult putObjectResult = ossClient.putObject(bucketName,key, file);
        // 关闭client
        ossClient.shutdown();
        return putObjectResult;
    }

}
