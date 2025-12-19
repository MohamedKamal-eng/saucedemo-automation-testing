package com.saucedemo.pages;

import com.saucedemo.utilities.config.PropertiesUtils;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected static WebDriver driver;
    private final By errorMessage  = By.cssSelector("[data-test='error']");


    protected BasePage() {
        driver = DriverManager.getDriver();
    }


    public String getTitle() {
        return driver.getTitle();
    }


    /**
     * Used by negative login tests.
     * Returns true if an error banner is shown.
     */
    public boolean isErrorDisplayed() {
        try {
            WaitHelpers.waitForVisibility(errorMessage);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Returns the error message text if present,
     * otherwise an empty string.
     */
    public String getErrorMessageText() {
        try {
            return ElementActions.getText(errorMessage);
        } catch (NoSuchElementException e) {
            return "";
        }
    }
    public void refreshPage() {
        DriverManager.getDriver().navigate().refresh();
    }

    public void navigateBack() {
        DriverManager.getDriver().navigate().back();
    }

    protected String getBaseUrl() {
        String url = PropertiesUtils.getProperty("base.url");
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("base.url must be configured in testConfig.properties");
        }
        return url;
    }

    /**
     * Generic direct navigation to a relative path (e.g. cart.html, checkout-step-one.html).
     */
    public void openDirect(String relativePath) {
        DriverManager.getDriver().get(getBaseUrl() + relativePath);
    }


}
