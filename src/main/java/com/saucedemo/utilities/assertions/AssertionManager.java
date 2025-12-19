package com.saucedemo.utilities.assertions;

import org.testng.ITestResult;

public class AssertionManager {
        private static final ThreadLocal<CustomSoftAssert> softAssert =
                ThreadLocal.withInitial(CustomSoftAssert::new);

        public static CustomSoftAssert getSoftAssert() {
            return softAssert.get();
        }

        public static void assertTrue(boolean condition, String message) {
            getSoftAssert().assertTrue(condition, message);
        }

        public static void assertFalse(boolean condition, String message) {
            getSoftAssert().assertFalse(condition, message);
        }

        public static void assertEquals(Object actual, Object expected, String message) {
            getSoftAssert().assertEquals(actual, expected, message);
        }

        public static void assertNotEquals(boolean condition, String message) {
            getSoftAssert().assertNotEquals(condition, message);
        }

        public static void assertAll(ITestResult result) {
            try {
                getSoftAssert().assertAll();
            } finally {
                softAssert.remove();
            }
        }
    }

