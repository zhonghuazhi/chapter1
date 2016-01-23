package org.smart4j.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhonghua on 16/1/5.
 */
public final class PropsUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName) {

        Properties properties = null;
        InputStream inputStream = null;

        try {
            inputStream =
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

            if (null == inputStream) {
                throw new FileNotFoundException(fileName + " is Not Found!");
            }

            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return properties;
    }

    /**
     * @param properties
     * @param key
     * @return
     */
    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }

    /**
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.contains(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }
}