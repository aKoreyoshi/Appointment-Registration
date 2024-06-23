package com.mac.ghpt.utils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月10日, 20:29:16
 */
public class FileProcessingUtil {

    // 判断当前文件是否存在
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    // 替换文件
    public static void replaceFile(String filePath, String newFilePath) {
        // 新文件路径
        Path newPath = Paths.get(newFilePath);
        // 旧文件路径
        Path oldPath = Paths.get(filePath);
        // 替换
        try {
            Files.copy(newPath,oldPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 删除文件
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            // 删除
            file.delete();
        }
    }
}
