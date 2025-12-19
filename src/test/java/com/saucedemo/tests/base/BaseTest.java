package com.saucedemo.tests.base;

import com.saucedemo.utilities.assertions.AssertionManager;
import com.saucedemo.utilities.config.PropertiesUtils;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import com.saucedemo.utilities.selenium.driver.DriverManagerFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;


@Epic("SauceDemo Web Application")
@Feature("Test Infrastructure & Environment Setup")
@Owner("Mohamed Kamal")
public class BaseTest {

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeSuite(alwaysRun = true)
    @Step("Write Allure environment information")
    public void setupAllureEnvironment(){
        com.saucedemo.utilities.reporting.AllureUtils.writeEnvironmentInfo();
    }
    @BeforeMethod(alwaysRun = true)
    @Step("Initialize WebDriver and navigate to base URL")
    public void setUp() {
        // 1) Start the driver via the factory
        DriverManagerFactory.initDriver();

        // 2) Get the driver from DriverManager
        driver = DriverManager.getDriver();

        // 3) Read base URL from properties (no hard-coding)
        baseUrl = PropertiesUtils.getProperty("base.url");
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException(
                    "Property 'base.url' is missing or empty in testConfig.properties."
            );
        }

        // 4) Navigate to the base URL
        driver.get(baseUrl);
    }

    @BeforeMethod(alwaysRun = true)
    @Step("Initialize soft assertion context")
    public void setupSoftAssert() {
        AssertionManager.getSoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    @Step("Quit WebDriver after test")
    public void tearDown() {
        // 5) Close and clean up driver after each test
        DriverManager.quitDriver();
    }

    @AfterMethod
    @Step("Verify and report all soft assertions")
    public void assertAllSoftAssert(ITestResult result) {
        AssertionManager.assertAll(result);
    }
}
