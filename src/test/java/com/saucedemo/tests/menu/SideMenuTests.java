package com.saucedemo.tests.menu;

import com.saucedemo.pageComponents.SideMenuComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.base.LoginBaseTest;
import com.saucedemo.tests.utils.dataProviders.InventoryDataProvider;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Side Menu – Navigation & Session Actions")
@Owner("Mohamed Kamal")
public class SideMenuTests extends LoginBaseTest {

    // ---------- Tests ----------

    /**
     * TC_MENU_001 - All Items should navigate back to Inventory page from another page (e.g., Cart).
     */
    @Test(
            description = "All Items from side menu navigates back to Inventory page from Cart",
            groups = {"menu", "regression"}
    )
    @Story("Side Menu – All Items navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that using 'All Items' from the side menu while on the Cart page returns the user to the Inventory page.")
    public void TC_MENU_001_allItemsNavigatesToInventory() {

        CartPage cartPage = new CartPage();
        InventoryPage inventoryPage = new InventoryPage();
        SideMenuComponent sideMenu = new SideMenuComponent();

        // Navigate away from inventory (go to Cart)
        cartPage.openCartFromHeader();

        // Use side menu: All Items
        sideMenu.clickAllItems();

        Assert.assertTrue(
                inventoryPage.isOnInventoryPage(),
                "After clicking All Items from side menu, user should be on Inventory page."
        );
    }

    /**
     * TC_MENU_002 - About link navigates to the configured About URL.
     */
    @Test(
            description = "About link in side menu navigates to configured external URL",
            groups = {"menu", "regression"}
    )
    @Story("Side Menu – About navigation")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that the 'About' link in the side menu navigates to the external URL configured in testConfig.properties.")
    public void TC_MENU_002_aboutNavigatesToConfiguredUrl() {

        SideMenuComponent sideMenu = new SideMenuComponent();

        // Click About and let SideMenuComponent handle navigation + URL reading
        String actualUrl        = sideMenu.clickAboutAndGetTargetUrl();
        String expectedUrlStart = sideMenu.getExpectedAboutUrlPrefix();

        Assert.assertTrue(
                actualUrl.startsWith(expectedUrlStart),
                "About link should navigate to URL starting with configured about.url. " +
                        "Expected start: " + expectedUrlStart + ", actual: " + actualUrl
        );
    }

    /**
     * TC_MENU_003 - Reset App State clears cart badge and removes items from cart.
     */
    @Test(
            description = "Reset App State clears cart items and cart badge",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"menu", "regression"}
    )
    @Story("Side Menu – Reset App State")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that using 'Reset App State' from the side menu clears all items from the cart and resets the cart badge to zero.")
    public void TC_MENU_003_resetAppStateClearsCart(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage           = new CartPage();
        SideMenuComponent sideMenu  = new SideMenuComponent();

        // Add an item to cart
        int initialBadge = inventoryPage.getCartBadgeCount();
        inventoryPage.addItemToCartById(item.getItemId());
        int badgeAfterAdd = inventoryPage.getCartBadgeCount();

        Assert.assertEquals(
                badgeAfterAdd,
                initialBadge + 1,
                "Cart badge should increase by 1 after adding an item before reset."
        );

        // Reset app state from side menu
        cartPage.openCartFromHeader();
        sideMenu.clickResetAppState();

        // Badge should be cleared
        int badgeAfterReset = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(
                badgeAfterReset,
                0,
                "Cart badge count should be 0 after Reset App State."
        );

        // Cart page should also be empty
        int cartItemsCount = cartPage.getCartItemsCount();

        Assert.assertEquals(
                cartItemsCount,
                0,
                "Cart should be empty after Reset App State."
        );
    }

    /**
     * TC_MENU_004 - Logout via side menu redirects to login page (base URL).
     */
    @Test(
            description = "Logout via side menu redirects to login page",
            groups = {"menu", "smoke", "regression"}
    )
    @Story("Side Menu – Logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that using 'Logout' from the side menu ends the session and redirects the user back to the login page (base URL).")
    public void TC_MENU_004_logoutRedirectsToLoginPage() {

        SideMenuComponent sideMenu = new SideMenuComponent();
        LoginPage loginPage        = new LoginPage();

        // Logout via side menu
        sideMenu.logout();

        Assert.assertTrue(
                loginPage.isOnLoginPage(),
                "After logout, user should be redirected to login page."
        );
    }
}
