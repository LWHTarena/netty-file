package com.lwhtarena.netty.tutorial03.util;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：
 */
public class FileTransferProperties {
    private static Properties pro = new Properties();

    public static void load(String path) {
        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
        Resource resource = fileSystemResourceLoader.getResource(path);
        try {
            pro.load(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String key, String defaultValue) {
        String value = (String) pro.get(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    public static String getString(String key) {
        String value = (String) pro.get(key);
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        Object value = pro.get(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value.toString());
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        Object value = (Boolean) pro.get(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.toString());
    }
}
