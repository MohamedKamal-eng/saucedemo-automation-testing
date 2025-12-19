package com.saucedemo.pages;

import com.saucedemo.utilities.config.PropertiesUtils;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import com.saucedemo.utilities.selenium.helperClasses.BrowserUtils;
import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

/**
 * Page Object for the SauceDemo login page.
 */
public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage  = By.cssSelector("[data-test='error']");
    private final By inventoryContainer = By.id("inventory_container");

    public LoginPage() {
        super();
    }

    public LoginPage enterUsername(String username) {
        ElementActions.type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        ElementActions.type(passwordInput, password);
        return this;
    }

    public LoginPage clickLogin() {
        ElementActions.click(loginButton);
        return this;
    }

    /**
     * Convenience method used by tests:
     * type username + password + click login.
     */
    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Used by tests to verify that a valid login
     * actually landed on the Inventory page.
     */
    public boolean waitUntilOnInventoryPage() {
        try {
            WaitHelpers.waitForVisibility(inventoryContainer);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isOnLoginPage() {
        String url = BrowserUtils.getCurrentUrl();
        if (url == null) return false;

        return url.endsWith("/") ||
                url.contains("index.html") ||
                url.contains("login");
    }



}
