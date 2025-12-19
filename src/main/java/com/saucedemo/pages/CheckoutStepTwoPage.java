package com.saucedemo.pages;

import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


/**
 * Checkout Step Two page (Overview).
 * Displays order summary, item list, and allows finishing or cancelling checkout.
 */
public class CheckoutStepTwoPage extends BasePage {

    private final By title = By.className("title"); // "Checkout: Overview"

    // Items in summary
    private final By cartItemContainer = By.className("cart_item");
    private final By itemNameLocator = By.className("inventory_item_name");
    private final By itemPriceLocator = By.className("inventory_item_price");

    // Summary info (payment, shipping, totals)
    private final By summaryInfoContainer = By.className("summary_info");
    private final By summaryValueLabels   = By.className("summary_value_label");
    private final By itemTotalLabel       = By.className("summary_subtotal_label");
    private final By taxLabel             = By.className("summary_tax_label");
    private final By totalLabel           = By.className("summary_total_label");

    // Buttons
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    /**
     * Checks if we are on Checkout Step Two page by verifying the title.
     */
    public boolean isOnCheckoutStepTwoPage() {
        WaitHelpers.waitForVisibility(title);
        String txt = ElementActions.getText(title).trim();
        return "Checkout: Overview".equalsIgnoreCase(txt);
    }

    // ---------- Items ----------

    /**
     * Returns the number of items shown in the checkout overview.
     */
    public int getItemsCount() {
        return ElementActions.count(cartItemContainer);

    }


    // ---------- Summary text (optional, useful for later tests) ----------

    public String getItemTotalText() {
        WaitHelpers.waitForVisibility(itemTotalLabel);
        return ElementActions.getText(itemTotalLabel).trim();
    }

    public String getTaxText() {
        WaitHelpers.waitForVisibility(taxLabel);
        return ElementActions.getText(taxLabel).trim();
    }

    public String getTotalText() {
        WaitHelpers.waitForVisibility(totalLabel);
        return ElementActions.getText(totalLabel).trim();
    }

    // ---------- Buttons ----------

    public void clickFinish() {
        ElementActions.click(finishButton);
    }

    public void clickCancel() {
        ElementActions.click(cancelButton);

    }

    // ---------- Payment & Shipping info ----------

    // ---------- Payment & Shipping info ----------

    public String getPaymentInformation() {
        // Wait for the summary container to be visible
        WaitHelpers.waitForVisibility(summaryInfoContainer);

        WebElement container = driver.findElement(summaryInfoContainer);
        // Inside it, values appear in order: [0] = payment, [1] = shipping
        java.util.List<WebElement> values = container.findElements(summaryValueLabels);

        if (values.isEmpty()) {
            throw new IllegalStateException("No summary value labels found in summary_info.");
        }

        return ElementActions.getText(values.get(0)).trim();
    }

    public String getShippingInformation() {
        WaitHelpers.waitForVisibility(summaryInfoContainer);

        WebElement container = driver.findElement(summaryInfoContainer);
        java.util.List<WebElement> values = container.findElements(summaryValueLabels);

        if (values.size() < 2) {
            throw new IllegalStateException("Shipping info not found – expected at least 2 summary_value_label elements.");
        }

        return ElementActions.getText(values.get(1)).trim();
    }

    private double extractAmount(String labelText) {
        if (labelText == null) {
            return 0.0;
        }

        int dollarIndex = labelText.indexOf('$');
        if (dollarIndex == -1 || dollarIndex == labelText.length() - 1) {
            return 0.0;
        }

        String numberPart = labelText.substring(dollarIndex + 1).trim();
        try {
            return Double.parseDouble(numberPart);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getItemTotalAmount() {
        return extractAmount(getItemTotalText());
    }

    public double getTaxAmount() {
        return extractAmount(getTaxText());
    }

    public double getTotalAmount() {
        return extractAmount(getTotalText());
    }

}
