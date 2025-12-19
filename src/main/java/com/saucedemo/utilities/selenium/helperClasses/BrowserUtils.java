package com.saucedemo.utilities.selenium.helperClasses;


import com.saucedemo.utilities.logging.LogsUtils;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import org.apache.logging.log4j.Logger;

public class BrowserUtils {

    private static final Logger log = LogsUtils.getLogger(BrowserUtils.class);

    public static void openUrl(String url) {
        DriverManager.getDriver().get(url);
        log.info("Opened URL: {}", url);
    }

    public static void refresh() {
        DriverManager.getDriver().navigate().refresh();
        log.info("Browser refreshed.");
    }

    public static void back() {
        DriverManager.getDriver().navigate().back();
        log.info("Navigated back.");
    }

    public static void forward() {
        DriverManager.getDriver().navigate().forward();
        log.info("Navigated forward.");
    }

    public static String getCurrentUrl() {
        String url = DriverManager.getDriver().getCurrentUrl();
        log.info("Current URL: {}", url);
        return url;
    }

    public static String getTitle() {
        String title = DriverManager.getDriver().getTitle();
        log.info("Page title: {}", title);
        return title;
    }
}