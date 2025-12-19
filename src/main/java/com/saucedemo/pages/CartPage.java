package com.saucedemo.pages;

import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import org.openqa.selenium.By;


public class CartPage extends BasePage {

    // Locators
    private static final By CART_ICON = By.id("shopping_cart_container");
    private static final By CART_ITEM_CONTAINER = By.cssSelector(".cart_item");
    private static final By ITEM_NAME = By.cssSelector(".inventory_item_name");
    private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By TITLE = By.className("title"); // "Your Cart"



    // ---------- Navigation ----------

    /**
     * Clicks on the cart icon in the header to open the cart page.
     */
    public void openCartFromHeader() {
        ElementActions.click(CART_ICON);
    }

    public boolean isOnCartPage() {
        String txt = ElementActions.getText(TITLE);
        return "Your Cart".equalsIgnoreCase(txt);
    }

    /**
     * Returns the current URL (useful to assert navigation).
     */

    // ---------- Cart items ----------

    /**
     * Returns the number of items currently displayed in the cart.
     */
    public int getCartItemsCount() {
        return ElementActions.count(CART_ITEM_CONTAINER);
    }


    /**
     * Removes an item from the cart using its underlying Swag Labs item id,
     * e.g. itemId = "sauce-labs-backpack" -> remove button id = "remove-sauce-labs-backpack".
     */
    public void removeItemById(String itemId) {
        String removeButtonId = "remove-" + itemId;
        By removeButton = By.id(removeButtonId);
        ElementActions.click(removeButton);
    }

    // ---------- Buttons ----------

    /**
     * Clicks the Continue Shopping button (should return to Inventory page).
     */
    public void clickContinueShopping() {
        ElementActions.click(CONTINUE_SHOPPING_BUTTON);
    }

    /**
     * Clicks the Checkout button (should navigate to Checkout Step One page).
     */
    public void clickCheckout() {
        ElementActions.click(CHECKOUT_BUTTON);
    }
}
