package com.mac.ghpt.oss.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.oss.properties.AliyunOssPropertiess;
import com.mac.ghpt.oss.service.OssService;
import com.mac.ghpt.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年05月29日, 16:33:43
 */
@Service
public class OssServiceImpl implements OssService {

    private AliyunOssPropertiess aliyunOssPropertiess;

    @Autowired
    public OssServiceImpl(AliyunOssPropertiess aliyunOssPropertiess) {
        this.aliyunOssPropertiess = aliyunOssPropertiess;
    }

    @Override
    public String upload(MultipartFile file) {
        // 配置Endpoint
        String endpoint = "https://" + aliyunOssPropertiess.getEndpoint();
        // 获取aliyun的AccessKeyId和AccessKeySecret
        String accessKeyId = aliyunOssPropertiess.getAccessKeyId();
        String accessKeySecret = aliyunOssPropertiess.getAccessKeySecret();
        // 填写Bucket名称
        String bucketName = aliyunOssPropertiess.getBucketName();


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 上传文件的流
            InputStream inputStream = file.getInputStream();
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 使用UUID生成随机字符与文件名拼接，保证文件名的唯一性
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            // 创建文件夹，保证一天中上传的文件都在一个文件夹内
            String dateString = DateUtil.format(DateUtil.date(), "yyyyMMdd");
            fileName = dateString + "/" + fileName;
            // 上传
            ossClient.putObject(bucketName, fileName, inputStream);
            // 获取上传文件的url
            String url = "https://" + bucketName + "." + aliyunOssPropertiess.getEndpoint() + "/" + fileName;

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GhptException(ResultCodeEnum.FILE_UPLOAD_FAILER);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}