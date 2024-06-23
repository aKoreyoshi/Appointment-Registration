package com.mac.ghpt.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年05月29日, 16:33:30
 */
public interface OssService {
    String upload(MultipartFile file);
}