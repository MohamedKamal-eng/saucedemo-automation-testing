package com.saucedemo.utilities.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE_PATH = "/properties/testConfig.properties";

    static {
        try (InputStream inputStream = PropertiesUtils.class.getResourceAsStream(CONFIG_FILE_PATH)) {
            if (inputStream == null) {
                throw new IllegalStateException(
                        "Configuration file not found on classpath: " + CONFIG_FILE_PATH);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + CONFIG_FILE_PATH, e);
        }
    }

    private PropertiesUtils() {}

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = PROPERTIES.getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}


