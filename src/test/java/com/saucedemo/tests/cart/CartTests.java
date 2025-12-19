package com.saucedemo.tests.cart;

import com.saucedemo.pageComponents.CartItemComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.tests.base.LoginBaseTest;
import com.saucedemo.tests.utils.dataProviders.InventoryDataProvider;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Cart Management")
@Owner("Mohamed Kamal")
public class CartTests extends LoginBaseTest {

    /**
     * TC_CART_001 - Verify cart is empty when no items have been added.
     */
    @Test(
            description = "Verify cart is empty when opened in a fresh logged-in session",
            groups = {"regression", "cart"}
    )
    @Story("View cart in a fresh session")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that a newly logged-in standard user sees an empty cart when opening it for the first time.")    public void TC_CART_001_cartIsEmptyOnFirstOpen() {

        CartPage cartPage = new CartPage();
        cartPage.openCartFromHeader();

        int itemCount = cartPage.getCartItemsCount();

        Assert.assertEquals(
                itemCount,
                0,
                "Cart should be empty when user has not added any items."
        );
    }

    /**
     * TC_CART_002 - Add single item from Inventory page and verify it appears in the cart.
     */
    @Test(
            description = "Add a single item from inventory and verify it appears in the cart and updates count",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"smoke", "regression", "cart"}
    )
    @Story("Add item to cart from inventory")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that when the user adds a single item from the inventory page, " +
            "the item appears in the cart and the cart items count is incremented by one.")
    public void TC_CART_002_addSingleItemFromInventory(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();

        // Add item from inventory
        int initialCartCount
                = inventoryPage.getCartBadgeCount();
        inventoryPage.addItemToCartById(item.getItemId());

        // Open cart and verify
        cartPage.openCartFromHeader();
        int cartItemsCount = cartPage.getCartItemsCount();

        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Added item '" + item.getItemName() + "' should appear in the cart."
        );

        Assert.assertEquals(
                cartItemsCount,
                initialCartCount + 1,
                "Number of cart items should increase by 1 after adding an item."
        );
    }

    /**
     * TC_CART_003 - Remove item from the cart and verify it disappears.
     */
    @Test(
            description = "Remove an existing item from the cart and verify it no longer appears",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"regression", "cart"}
    )
    @Story("Remove item from cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adds an item to the cart, verifies its presence, then removes it and checks " +
            "that it no longer appears and the cart items count is decreased.")
    public void TC_CART_003_removeItemFromCart(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();

        // Add item first
        inventoryPage.addItemToCartById(item.getItemId());

        // Open cart and verify presence
        cartPage.openCartFromHeader();
        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Precondition failed: item should be present in cart before removal."
        );

        int countBeforeRemove = cartPage.getCartItemsCount();

        // Remove item
        cartPage.removeItemById(item.getItemId());

        int countAfterRemove = cartPage.getCartItemsCount();

        Assert.assertFalse(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Item '" + item.getItemName() + "' should no longer be present in the cart after removal."
        );

        Assert.assertEquals(
                countAfterRemove,
                Math.max(0, countBeforeRemove - 1),
                "Cart items count should decrease by 1 after removal (or go to 0)."
        );
    }

    /**
     * TC_CART_004 - Continue Shopping from cart navigates back to Inventory page.
     */
    @Test(
            description = "Use Continue Shopping from cart and verify navigation back to inventory",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"regression", "cart", "navigation"}
    )
    @Story("Navigate from cart back to inventory")
    @Severity(SeverityLevel.MINOR)
    @Description("Ensures that clicking the Continue Shopping button from the cart navigates " +
            "the user back to the inventory page.")
    public void TC_CART_004_continueShoppingNavigatesBackToInventory(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();

        // Add any item then open cart
        inventoryPage.addItemToCartById(item.getItemId());
        cartPage.openCartFromHeader();

        // Click Continue Shopping
        cartPage.clickContinueShopping();


        Assert.assertTrue(
                inventoryPage.isOnInventoryPage(),
                "After clicking Continue Shopping, user should be back on Inventory page."
        );
    }

    /**
     * TC_CART_005 - Checkout from cart navigates to Checkout Step One page.
     */
    @Test(
            description = "Start checkout from cart and verify navigation to the first checkout step",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"smoke", "regression", "cart", "checkout"}
    )
    @Story("Start checkout from cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that clicking the Checkout button from the cart after adding an item " +
            "navigates the user to the Checkout Step One page.")
    public void TC_CART_005_checkoutNavigatesToCheckoutStepOne(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();
        CheckoutStepOnePage checkoutStepOnePage = new CheckoutStepOnePage();

        // Add any item then open cart
        inventoryPage.addItemToCartById(item.getItemId());
        cartPage.openCartFromHeader();

        // Click Checkout
        cartPage.clickCheckout();

        // We don't hardcode full URL; just assert we reached checkout-step-one.html
        Assert.assertTrue(
                checkoutStepOnePage.isOnCheckoutStepOnePage(),
                "After clicking Checkout from cart, user should be on Checkout Step One page"
        );
    }

    /**
     * TC_CART_006 - Verify system behavior when attempting to check out with an empty cart
     */
    @Test(
            description = "Attempt to start checkout with an empty cart and verify user does not reach checkout step one",
            groups = {"regression", "cart", "negative"}
    )
    @Story("Prevent checkout from empty cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Opens the cart without adding any items, clicks Checkout, and verifies that the user " +
            "is not navigated to the Checkout Step One page.")
    public void TC_CART_006_checkoutUsingEmptyCart() {

        CartPage cartPage = new CartPage();
        CheckoutStepOnePage checkoutStepOnePage = new CheckoutStepOnePage();

        cartPage.openCartFromHeader();
        cartPage.clickCheckout();

        Assert.assertFalse(
                checkoutStepOnePage.isOnCheckoutStepOnePage(),
                "After clicking Checkout from Empty cart, user should not be on Checkout Step One page."
        );
    }
}
