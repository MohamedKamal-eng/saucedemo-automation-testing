package com.saucedemo.utilities.selenium.helperClasses;

import com.saucedemo.utilities.config.PropertiesUtils;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitHelpers {

    private static final String EXPLICIT_WAIT_KEY = "explicit.wait.seconds";

    private WaitHelpers() {}

    private static long getExplicitWaitSeconds() {
        String value = PropertiesUtils.getProperty(EXPLICIT_WAIT_KEY);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Property '" + EXPLICIT_WAIT_KEY + "' is missing or empty in testConfig.properties.");
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Property '" + EXPLICIT_WAIT_KEY + "' must be a valid number (seconds). Current value: " + value, e);
        }
    }

    private static WebDriverWait getWait() {
        WebDriver driver = DriverManager.getDriver();
        long timeoutSeconds = getExplicitWaitSeconds();
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public static WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisibility(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}
