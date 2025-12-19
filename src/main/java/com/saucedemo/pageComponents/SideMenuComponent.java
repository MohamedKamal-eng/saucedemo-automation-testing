package com.saucedemo.pageComponents;

import com.saucedemo.pages.BasePage;
import com.saucedemo.utilities.config.PropertiesUtils;
import com.saucedemo.utilities.selenium.helperClasses.BrowserUtils;
import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;

/**
 * Represents the left side menu (burger menu) on SauceDemo.
 * Used for actions like Logout, All Items, Reset App State.
 */
public class SideMenuComponent extends BasePage {

    // Burger menu button
    private final By menuButton = By.id("react-burger-menu-btn");

    // Side menu links
    private final By allItemsLink    = By.id("inventory_sidebar_link");
    private final By aboutLink       = By.id("about_sidebar_link");
    private final By logoutLink      = By.id("logout_sidebar_link");
    private final By resetAppLink    = By.id("reset_sidebar_link");

    // Container for menu (to confirm it is open)
    private final By menuContainer   = By.id("react-burger-menu-btn"); // menu button stays visible

    public void openMenu() {
        ElementActions.click(menuButton);
        // small wait to ensure menu is interactive
        WaitHelpers.waitForVisibility(allItemsLink);
    }

    public void clickAllItems() {
        openMenu();
        ElementActions.click(allItemsLink);
    }

    public void clickAbout() {
        openMenu();
        ElementActions.click(aboutLink);
    }

    public void clickResetAppState() {
        openMenu();
        ElementActions.click(resetAppLink);
    }

    public void logout() {
        openMenu();
        ElementActions.click(logoutLink);
    }

    public String clickAboutAndGetTargetUrl() {
        // Existing method that opens About
        clickAbout();

        String url = BrowserUtils.getCurrentUrl();
        if (url == null) {
            throw new IllegalStateException("Current URL is null after clicking About.");
        }
        return url;
    }

    public String getExpectedAboutUrlPrefix() {
        String expected = PropertiesUtils.getProperty("about.url");
        if (expected == null || expected.isBlank()) {
            throw new IllegalStateException("about.url property must be set in testConfig.properties");
        }
        return expected;
    }

}
