package com.saucedemo.tests.utils.listeners;

import com.saucedemo.utilities.reporting.AllureUtils;
import org.openqa.selenium.*;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        // 1) Screenshot
        AllureUtils.attachScreenshot("Failure Screenshot");

        // 2) Full stacktrace as text
        AllureUtils.attachText("Failure Reason", getStackTrace(result));
    }

    private String getStackTrace(ITestResult result) {
        if (result.getThrowable() == null) {
            return "No stacktrace available";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        result.getThrowable().printStackTrace(pw);
        return sw.toString();
    }
}