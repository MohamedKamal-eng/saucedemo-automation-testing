package com.saucedemo.tests.e2e;

import com.saucedemo.pageComponents.SideMenuComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.base.BaseTest;
import com.saucedemo.tests.utils.dataProviders.E2EDataProvider;
import com.saucedemo.tests.utils.models.OrderModel;
import com.saucedemo.tests.utils.models.UserModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("E2E – Application State Management")
@Owner("Mohamed Kamal")
public class ResetAppStateE2ETest extends BaseTest {

    /**
     * TC_E2E_003 - Reset App State clears all items from cart and badge for an existing order.
     */
    @Test(
            description = "Reset App State should clear all items from cart and reset the cart badge",
            dataProvider = "standardTwoItemsOrderWithUser",
            dataProviderClass = E2EDataProvider.class,
            groups = {"regression", "e2e", "cart", "menu"}
    )
    @Story("Reset app state from side menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description(
            "Logs in as the standard user, adds a multi-item order to the cart, then uses the 'Reset App State' " +
                    "option from the side menu. Verifies that the cart badge returns to zero and that the cart is empty."
    )
    public void TC_E2E_003_resetAppStateClearsCartAndBadge(OrderModel order, UserModel standardUser) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage           = new CartPage();
        SideMenuComponent sideMenu  = new SideMenuComponent();
        LoginPage loginPage         = new LoginPage();

        // Login as standard user
        loginPage.loginAs(standardUser.getUsername(), standardUser.getPassword());

        int initialBadge = inventoryPage.getCartBadgeCount();

        // 1) Add all items in order to cart
        for (String itemId : order.getItemIds()) {
            // We don’t need TestDataUtils here; itemId is enough
            inventoryPage.addItemToCartById(itemId);
        }

        int badgeAfterAdd = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(
                badgeAfterAdd,
                initialBadge + order.getItemIds().size(),
                "Cart badge should increase by number of added items."
        );

        cartPage.openCartFromHeader();
        int itemsInCartBeforeReset = cartPage.getCartItemsCount();
        Assert.assertTrue(
                itemsInCartBeforeReset >= order.getItemIds().size(),
                "Precondition: cart should contain at least the items from the order before reset."
        );

        // 2) Reset from side menu
        sideMenu.clickResetAppState();

        // 3) Cart badge should be 0
        int badgeAfterReset = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(
                badgeAfterReset,
                0,
                "Cart badge should be 0 after Reset App State."
        );

        // 4) Cart should be empty
        int itemsInCartAfterReset = cartPage.getCartItemsCount();

        Assert.assertEquals(
                itemsInCartAfterReset,
                0,
                "Cart should be empty after Reset App State."
        );
    }
}
