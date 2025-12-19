package com.saucedemo.tests.e2e;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.base.BaseTest;
import com.saucedemo.tests.utils.dataProviders.LoginDataProvider;
import com.saucedemo.tests.utils.models.UserModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("SauceDemo Web Application")
@Feature("E2E – Smoke Testing")
@Owner("Mohamed Kamal")
public class SmokeOpenInventoryTest extends BaseTest {


    /**
     * TC_E2E_000 - Smoke: standard user can log in and Inventory shows at least one item.
     */
    @Test(
            description = "Smoke Test: standard user should log in and see items on Inventory page",
            dataProvider = "standardUser",
            dataProviderClass = LoginDataProvider.class,
            groups = {"smoke", "e2e", "login", "inventory"}
    )
    @Story("Basic login and landing page validation")
    @Severity(SeverityLevel.BLOCKER)
    @Description(
            "Performs a minimal smoke validation: logs in using the standard user and verifies that the " +
                    "Inventory page is displayed and contains at least one product item."
    )
    public void TC_E2E_000_smokeOpenInventory(UserModel user) {

        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = new InventoryPage();

        // 1) Login
        loginPage.loginAs(user.getUsername(), user.getPassword());
        boolean onInventory = loginPage.waitUntilOnInventoryPage();

        Assert.assertTrue(
                onInventory,
                "Standard user should reach Inventory page after login."
        );

        // 2) Inventory should show at least one item
        List<String> itemNames = inventoryPage.getAllItemNames();

        Assert.assertFalse(
                itemNames.isEmpty(),
                "Inventory should contain at least one item."
        );
    }
}
