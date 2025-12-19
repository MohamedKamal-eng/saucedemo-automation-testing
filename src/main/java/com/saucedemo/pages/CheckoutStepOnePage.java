package com.saucedemo.pages;

import com.saucedemo.utilities.selenium.helperClasses.BrowserUtils;
import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;

/**
 * Checkout Step One page (Your Information).
 * Handles entering user info and continuing / cancelling checkout.
 */
public class CheckoutStepOnePage extends BasePage {

    private final By title = By.className("title"); // "Checkout: Your Information"

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");

    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");

    private final By errorMessageContainer = By.cssSelector("h3[data-test='error']");

    public boolean isOnCheckoutStepOnePage() {
        String currentUrl = BrowserUtils.getCurrentUrl();
        return currentUrl != null && currentUrl.contains("checkout-step-one.html");
    }

    // ---------- Form interactions ----------

    public void enterFirstName(String firstName) {
        WaitHelpers.waitForVisibility(firstNameInput);
        ElementActions.type(firstNameInput , firstName);
    }

    public void enterLastName(String lastName) {
        WaitHelpers.waitForVisibility(lastNameInput);
        ElementActions.type(lastNameInput , lastName);
    }

    public void enterPostalCode(String postalCode) {
        WaitHelpers.waitForVisibility(postalCodeInput);
        ElementActions.type(postalCodeInput , postalCode);
    }

    public void fillUserInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    // ---------- Buttons ----------

    public void clickContinue() {
        ElementActions.click(continueButton);
    }

    public void clickCancel() {
        ElementActions.click(cancelButton);
    }

    // ---------- Read current field values (for “not persisted” checks) ----------
    public String getFirstNameValue() {
        return ElementActions.getValue(firstNameInput);
    }

    public String getLastNameValue() {
        return ElementActions.getValue(lastNameInput);
    }

    public String getPostalCodeValue() {
        return ElementActions.getValue(postalCodeInput);
    }



}
