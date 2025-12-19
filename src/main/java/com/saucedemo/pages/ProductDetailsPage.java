package com.saucedemo.pages;

import com.saucedemo.utilities.selenium.helperClasses.BrowserUtils;
import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;

public class ProductDetailsPage extends BasePage {


    // Locators for product details page
    private static final By PRODUCT_NAME = By.cssSelector(".inventory_details_name.large_size");
    private static final By PRODUCT_DESCRIPTION = By.cssSelector(".inventory_details_desc.large_size");
    private static final By PRODUCT_PRICE = By.cssSelector(".inventory_details_price");
    private static final By BACK_TO_PRODUCTS_BUTTON = By.id("back-to-products");

    // ✅ On details page, the buttons are generic, without itemId
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("button[data-test='add-to-cart']");
    private static final By REMOVE_BUTTON = By.cssSelector("button[data-test='remove']");


    // ---------- Getters ----------

    public String getProductName() {
        WaitHelpers.waitForVisibility(PRODUCT_NAME);
        return ElementActions.getText(PRODUCT_NAME).trim();
    }

    public String getProductDescription() {
        WaitHelpers.waitForVisibility(PRODUCT_DESCRIPTION);
        return ElementActions.getText(PRODUCT_DESCRIPTION).trim();
    }

    public String getProductPriceText() {
        WaitHelpers.waitForVisibility(PRODUCT_PRICE);
        return ElementActions.getText(PRODUCT_PRICE).trim();
    }

    public boolean isOnProductDetailsPage() {
        String currentUrl = BrowserUtils.getCurrentUrl();
        return currentUrl != null && currentUrl.contains("inventory-item.html");
    }


    // ---------- Cart actions on details page ----------

    /**
     * On product details page there is a single generic add-to-cart button.
     * We keep the method signature for compatibility, but itemId is not needed here.
     */
    public void addItemToCartById(String itemId) {
        WaitHelpers.waitForVisibility(ADD_TO_CART_BUTTON);
        ElementActions.click(ADD_TO_CART_BUTTON);
    }

    /**
     * After adding from details page, the button becomes a generic "remove" button.
     */
    public void removeItemFromCartById(String itemId) {
        WaitHelpers.waitForVisibility(REMOVE_BUTTON);
        ElementActions.click(REMOVE_BUTTON);
    }

    // ---------- Navigation ----------

    /**
     * Clicks the Back to products button to return to Inventory page.
     */
    public void clickBackToProducts() {
        WaitHelpers.waitForVisibility(BACK_TO_PRODUCTS_BUTTON);
        ElementActions.click(BACK_TO_PRODUCTS_BUTTON);
    }
}
