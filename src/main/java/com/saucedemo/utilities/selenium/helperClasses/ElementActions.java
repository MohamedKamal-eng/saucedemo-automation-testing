package com.saucedemo.utilities.selenium.helperClasses;

import com.saucedemo.pages.BasePage;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementActions extends BasePage {

    private ElementActions() {}

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    public static void click(By locator) {
        WebElement element = WaitHelpers.waitForClickable(locator);
        element.click();
    }

    public static void click(WebElement parent, By locator) {
        parent.findElement(locator).click();
    }

    public static void type(By locator, String text) {
        WebElement element = WaitHelpers.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    public static void clear(By locator) {
        WebElement element = WaitHelpers.waitForVisibility(locator);
        element.clear();
    }

    public static String getText(By locator) {
        WebElement element = WaitHelpers.waitForVisibility(locator);
        return element.getText();
    }

    public static String getText(WebElement parent,By locator) {
        WebElement element = parent.findElement(locator);  // find inside the parent only
        return element.getText();
    }

    public static String getText(WebElement element) {
        return element.getText();
    }

    public static boolean isDisplayed(By locator) {
        try {
            return WaitHelpers.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPresent(By locator) {
        try {
            WaitHelpers.waitForPresence(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getValue(By locator) {
        WebElement element = WaitHelpers.waitForVisibility(locator);
        return element.getAttribute("value");
    }

    public static int count(By locator) {
        return driver().findElements(locator).size();
    }

}
