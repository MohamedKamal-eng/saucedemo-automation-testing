package com.saucedemo.pages;

import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;

/**
 * Checkout Complete page – final confirmation after finishing order.
 */
public class CheckoutCompletePage extends BasePage {

    private final By title = By.className("title"); // "Checkout: Complete!"
    private final By completeHeader = By.className("complete-header"); // "Thank you for your order!"
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public boolean isOnCheckoutCompletePage() {
        WaitHelpers.waitForVisibility(title);
        String txt = ElementActions.getText(title).trim();
        return "Checkout: Complete!".equalsIgnoreCase(txt);
    }

    public String getCompleteHeaderText() {
        WaitHelpers.waitForVisibility(completeHeader);
        return ElementActions.getText(completeHeader).trim();
    }

    public String getCompleteMessageText() {
        WaitHelpers.waitForVisibility(completeText);
        return ElementActions.getText(completeText).trim();
    }

    public void clickBackHome() {
        WaitHelpers.waitForVisibility(backHomeButton);
        ElementActions.click(backHomeButton);
    }
}
