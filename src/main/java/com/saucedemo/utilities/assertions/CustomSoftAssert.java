package com.saucedemo.utilities.assertions;

import org.testng.asserts.SoftAssert;
import java.util.ArrayList;
import java.util.List;

public class CustomSoftAssert extends SoftAssert {

    private final List<String> failures = new ArrayList<>();

    @Override
    public void onAssertFailure(org.testng.asserts.IAssert<?> assertCommand, AssertionError ex) {
        failures.add(ex.getMessage());
        super.onAssertFailure(assertCommand, ex);
    }

    public boolean hasFailures() {
        return !failures.isEmpty();
    }

    public List<String> getFailures() {
        return failures;
    }
}
