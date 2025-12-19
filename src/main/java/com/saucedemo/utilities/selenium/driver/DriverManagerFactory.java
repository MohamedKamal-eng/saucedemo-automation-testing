package com.saucedemo.utilities.selenium.driver;

import com.saucedemo.utilities.config.PropertiesUtils;
import org.openqa.selenium.WebDriver;

public class DriverManagerFactory {

    private static final String BROWSER_PROPERTY_KEY = "browser";

    private DriverManagerFactory() {}

    public static void initDriver() {
        String browserName = PropertiesUtils.getProperty(BROWSER_PROPERTY_KEY);

        if (browserName == null || browserName.isBlank()) {
            throw new IllegalStateException("Property '" + BROWSER_PROPERTY_KEY + "' is missing or empty in testConfig.properties.");
        }

        WebDriver webDriver = DriverFactory.createDriver(browserName);
        DriverManager.setDriver(webDriver);
    }
}
