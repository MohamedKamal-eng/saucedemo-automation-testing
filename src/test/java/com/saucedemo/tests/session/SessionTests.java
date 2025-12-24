package com.saucedemo.tests.session;

import com.saucedemo.pageComponents.CartItemComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.tests.base.LoginBaseTest;
import com.saucedemo.tests.utils.dataProviders.InventoryDataProvider;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Session & State Persistence")
@Owner("Mohamed Kamal")
public class SessionTests extends LoginBaseTest {

    /**
     * TC_SESS_001 - Verify cart state is preserved after refreshing Inventory page.
     * Steps:
     *  1) Logged in (handled by LoginBaseTest).
     *  2) Add item to cart from Inventory.
     *  3) Verify cart badge count increased.
     *  4) Refresh Inventory page.
     *  5) Verify cart badge count is still the same and item is still in cart.
     */
    @Test(
            description = "Cart state is preserved after refreshing Inventory page",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"session", "regression"}
    )
    @Story("Session persistence – Cart state across Inventory refresh")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that after adding an item to the cart from Inventory, refreshing the Inventory page " +
            "does not reset the cart badge or remove the item from the cart.")
    public void TC_SESS_001_cartStatePreservedAfterInventoryRefresh(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();

        int initialBadge = inventoryPage.getCartBadgeCount();

        // Add item to cart
        inventoryPage.addItemToCartById(item.getItemId());
        int badgeAfterAdd = inventoryPage.getCartBadgeCount();

        Assert.assertEquals(
                badgeAfterAdd,
                initialBadge + 1,
                "Cart badge should increase by 1 after adding item on Inventory page."
        );

        // Refresh Inventory page
        inventoryPage.refreshPage();

        // Badge should still reflect the item in cart
        int badgeAfterRefresh = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(
                badgeAfterRefresh,
                badgeAfterAdd,
                "Cart badge count should be preserved after refreshing Inventory page."
        );

        // Open cart and verify item is still there
        cartPage.openCartFromHeader();
        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Item should still be present in cart after Inventory page refresh."
        );
    }

    /**
     * TC_SESS_002 - Verify cart contents are preserved after refreshing Cart page.
     * Steps:
     *  1) Logged in (handled by LoginBaseTest).
     *  2) Add item to cart from Inventory and open Cart.
     *  3) Verify item exists in cart.
     *  4) Refresh Cart page.
     *  5) Verify item still exists in cart and item count is unchanged.
     */
    @Test(
            description = "Cart contents are preserved after refreshing Cart page",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"session", "regression"}
    )
    @Story("Session persistence – Cart state across Cart refresh")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that refreshing the Cart page does not change the number of items in the cart " +
            "and that previously added items remain visible.")
    public void TC_SESS_002_cartContentsPreservedAfterCartRefresh(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();

        // Add item to cart and open Cart
        inventoryPage.addItemToCartById(item.getItemId());
        cartPage.openCartFromHeader();

        int itemsCountBeforeRefresh = cartPage.getCartItemsCount();

        Assert.assertTrue(
                itemsCountBeforeRefresh > 0,
                "Precondition failed: there should be at least one item in cart before refreshing."
        );
        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Precondition failed: specific item should be present in cart before refreshing."
        );

        // Refresh Cart page
        cartPage.refreshPage();

        int itemsCountAfterRefresh = cartPage.getCartItemsCount();

        Assert.assertEquals(
                itemsCountAfterRefresh,
                itemsCountBeforeRefresh,
                "Number of items in cart should be preserved after refreshing Cart page."
        );
        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Item should still be present in cart after Cart page refresh."
        );
    }
}