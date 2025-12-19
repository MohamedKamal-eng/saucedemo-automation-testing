package com.saucedemo.utilities.selenium.driver;

import com.saucedemo.utilities.config.PropertiesUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    private static final String WINDOW_MAXIMIZED_KEY = "maximize.window";

    private DriverFactory() {}

    public static WebDriver createDriver(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name is null or empty. " +
                    "Check 'browser' property in testConfig.properties.");
        }

        return switch (browserName.toLowerCase()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browserName +
                            ". Update DriverFactory or change 'browser' in testConfig.properties.");
        };
    }

    private static boolean shouldStartMaximized() {
        String value = PropertiesUtils.getProperty(WINDOW_MAXIMIZED_KEY, "true");
        return Boolean.parseBoolean(value.trim());
    }


    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Disable password manager & leak detection
        options.addArguments("--disable-features=PasswordLeakDetection,PasswordLeakDetectionEnabled");
        options.addArguments("--disable-features=SafetyCheck");
        options.addArguments("--disable-popup-blocking");

        // Disable Chrome credential services
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("credentials_enable_service", false);
            put("profile.password_manager_enabled", false);
            put("password_manager_leak_detection_enabled", false);
        }});

        if (shouldStartMaximized()) {
            options.addArguments("--start-maximized");
        }

        return new ChromeDriver(options);
    }


    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);
        if (shouldStartMaximized()) {
            driver.manage().window().maximize();
        }
        return driver;
    }

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        WebDriver driver = new EdgeDriver(options);
        if (shouldStartMaximized()) {
            driver.manage().window().maximize();
        }
        return driver;
    }
}
