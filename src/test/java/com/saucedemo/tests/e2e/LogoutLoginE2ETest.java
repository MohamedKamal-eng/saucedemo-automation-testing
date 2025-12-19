package com.saucedemo.tests.e2e;

import com.saucedemo.pageComponents.CartItemComponent;
import com.saucedemo.pageComponents.SideMenuComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.base.BaseTest;
import com.saucedemo.tests.utils.dataProviders.E2EDataProvider;
import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.tests.utils.models.UserModel;
import com.saucedemo.utilities.assertions.AssertionManager;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Authentication & Session Management")
@Owner("Mohamed Kamal")
public class LogoutLoginE2ETest extends BaseTest {

    /**
     * TC_E2E_002 - Standard user logs out then logs in again successfully.
     */
    @Test(
            description = "Standard user logs out and logs in again, keeping cart items persisted",
            dataProvider = "standardUserWithItem",
            dataProviderClass = E2EDataProvider.class,
            groups = {"smoke", "regression", "e2e", "auth", "session"}
    )
    @Story("Logout and re-login as the same user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a standard user can log out and log back in successfully, " +
            "and that items added to the cart before logout are still present after re-login.")
    public void TC_E2E_002_logoutThenLoginAgain(UserModel standardUser, ItemModel item) {

        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = new InventoryPage();
        SideMenuComponent sideMenu = new SideMenuComponent();
        CartPage cartPage = new CartPage();

        // 1) Login first time
        loginPage.loginAs(standardUser.getUsername(), standardUser.getPassword());

        boolean onInventory = loginPage.waitUntilOnInventoryPage();
        Assert.assertTrue(
                onInventory,
                "Precondition failed: user should reach Inventory page after initial login."
        );

        // 2) Add item to cart
        inventoryPage.addItemToCartById(item.getItemId());
        int badgeAfterAdd = inventoryPage.getCartBadgeCount();

        AssertionManager.assertEquals(
                badgeAfterAdd,
                1,
                "Cart badge should be 1 after adding a single item before logout."
        );

        // 3) Logout
        sideMenu.logout();

        Assert.assertTrue(
                loginPage.isOnLoginPage(),
                "After logout, user should be on login page (base URL)."
        );

        // 4) Login again
        loginPage.loginAs(standardUser.getUsername(), standardUser.getPassword());
        boolean onInventoryAfterRelogin = loginPage.waitUntilOnInventoryPage();

        Assert.assertTrue(
                onInventoryAfterRelogin,
                "After logging in again, user should reach Inventory page."
        );

        // 5) Verify cart is persisted
        cartPage.openCartFromHeader();

        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "After re-login, item '" + item.getItemName() + "' should still be present in the cart."
        );

        int cartCountAfterRelogin = cartPage.getCartItemsCount();
        Assert.assertTrue(
                cartCountAfterRelogin >= 1,
                "After re-login, cart should contain at least one item."
        );
    }
}
