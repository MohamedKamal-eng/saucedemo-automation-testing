package com.saucedemo.utilities.reporting;


import com.saucedemo.utilities.config.PropertiesUtils;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureUtils {

    @Attachment(value = "{0}", type = "text/plain")
    public static String attachText(String name, String message) {
        return message == null ? "" : message;
    }

    @Attachment(value = "{0}", type = "image/png")
    public static byte[] attachScreenshot(String name) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver == null) return null;
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Writes environment.properties into the Allure results directory
     * so that the Environment tab appears in the report.
     */
    public static void writeEnvironmentInfo() {
        try {
            // This is set in pom.xml (maven-surefire-plugin)
            String resultsDir = System.getProperty(
                    "allure.results.directory",
                    "test_outputs/allure_results"
            );

            Path resultsPath = Paths.get(resultsDir);
            if (!Files.exists(resultsPath)) {
                Files.createDirectories(resultsPath);
            }

            File envFile = new File(resultsPath.toFile(), "environment.properties");
            try (FileWriter writer = new FileWriter(envFile)) {

                writer.write("Browser=edge\n");
                writer.write("Base URL=" + PropertiesUtils.getProperty("base.url") + "\n");
                writer.write("OS=" + System.getProperty("os.name") + "\n");
                writer.write("Java Version=" + System.getProperty("java.version") + "\n");
                writer.write("Author=Mohamed Kamal\n");
                writer.write("Project=SauceDemo Automation\n");
            }
        } catch (Exception e) {
            System.out.println("Failed to write Allure environment file: " + e.getMessage());
        }
    }
}

