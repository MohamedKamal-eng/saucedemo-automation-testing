package com.saucedemo.tests.security;

import com.saucedemo.pageComponents.SideMenuComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.base.BaseTest;
import com.saucedemo.tests.utils.dataProviders.E2EDataProvider;
import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.tests.utils.models.UserModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Security – Unauthorized access & session handling")
@Owner("Mohamed Kamal")
public class SecurityTests extends BaseTest {

    // ---------- TC_SEC_001 ----------

    /**
     * TC_SEC_001 - Verify direct access to Inventory page URL without login is not allowed.
     * Precondition: user is NOT logged in (BaseTest only opens base URL, no login).
     */
    @Test(
            description = "Direct access to the Inventory URL without login is blocked",
            groups = {"security", "regression"}
    )
    @Story("Access Control – Protect inventory from unauthenticated users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that navigating directly to the Inventory URL without logging in redirects the user back to the login page.")
    public void TC_SEC_001_directAccessInventoryWithoutLoginNotAllowed() {

        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = new InventoryPage();

        // Directly try to open the Inventory page without logging in
        loginPage.openDirect("inventory.html");

        // Should NOT actually be on inventory page
        Assert.assertFalse(
                inventoryPage.isOnInventoryPage(),
                "User should NOT be allowed to stay on Inventory page without login."
        );

        // Should be on login page instead
        Assert.assertTrue(
                loginPage.isOnLoginPage(),
                "After unauthorized access to Inventory, user should be on Login page."
        );
    }


    // ---------- TC_SEC_002 ----------

    /**
     * TC_SEC_002 - Verify direct access to Cart and Checkout pages without login is not allowed.
     */
    @Test(
            description = "Direct access to Cart and Checkout URLs without login is blocked",
            groups = {"security", "regression"}
    )
    @Story("Access Control – Protect cart and checkout from unauthenticated users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that direct access to Cart and Checkout URLs without authentication is blocked and redirected to the login page.")
    public void TC_SEC_002_directAccessCartAndCheckoutWithoutLoginNotAllowed() {

        LoginPage loginPage = new LoginPage();

        String[] restrictedPaths = {
                "cart.html",
                "checkout-step-one.html",
                "checkout-step-two.html",
                "checkout-complete.html"
        };

        for (String path : restrictedPaths) {
            // Fresh attempt per URL
            loginPage.openDirect(path);

            // For all restricted URLs, we expect to land on Login page
            Assert.assertTrue(
                    loginPage.isOnLoginPage(),
                    "When accessing '" + path + "' without login, user should end up on Login page."
            );
        }
    }


    // ---------- TC_SEC_003 ----------

    /**
     * TC_SEC_003 - Verify accessing Inventory / Checkout after logout using Back is not allowed.
     */
    @Test(
            description = "Browser Back cannot reopen Inventory or Checkout after logout",
            dataProvider = "standardUserWithItem",
            dataProviderClass = E2EDataProvider.class,
            groups = {"security", "regression"}
    )
    @Story("Session Security – Prevent access to protected pages after logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that after logging out, using the browser Back button does not allow access back to Inventory or Checkout pages.")
    public void TC_SEC_003_accessAfterLogoutUsingBackNotAllowed(UserModel standardUser, ItemModel item) {

        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();
        CheckoutStepOnePage stepOnePage = new CheckoutStepOnePage();
        SideMenuComponent sideMenu = new SideMenuComponent();

        // 1) Login as standard_user
        loginPage.loginAs(standardUser.getUsername(), standardUser.getPassword());

        boolean onInventory = loginPage.waitUntilOnInventoryPage();
        Assert.assertTrue(
                onInventory,
                "Precondition failed: could not log in and reach Inventory page."
        );

        // 2) Navigate deeper (Cart + Checkout Step One) so Back history contains restricted pages
        inventoryPage.addItemToCartById(item.getItemId());

        cartPage.openCartFromHeader();
        cartPage.clickCheckout();

        Assert.assertTrue(
                stepOnePage.isOnCheckoutStepOnePage(),
                "Precondition failed: user should be on Checkout Step One before logout."
        );

        // 3) Logout via side menu
        sideMenu.logout();

        // After logout, must be on login page
        Assert.assertTrue(
                loginPage.isOnLoginPage(),
                "After logout, user should be on Login page."
        );

        // 4) Use browser Back and verify restricted pages are not accessible
        loginPage.navigateBack();
        Assert.assertTrue(
                loginPage.isOnLoginPage(),
                "After using Back once post-logout, user should still be on Login page."
        );

        // To be extra safe, try Back again
        loginPage.navigateBack();
        Assert.assertTrue(
                loginPage.isOnLoginPage(),
                "Even after multiple Back operations, user should still be on Login page."
        );
    }
}
