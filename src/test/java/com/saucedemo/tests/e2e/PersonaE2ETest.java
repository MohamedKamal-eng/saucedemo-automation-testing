package com.saucedemo.tests.e2e;

import com.saucedemo.pageComponents.CartItemComponent;
import com.saucedemo.pages.*;
import com.saucedemo.tests.base.BaseTest;
import com.saucedemo.tests.utils.dataProviders.E2EDataProvider;
import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.tests.utils.models.UserModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Persona-based E2E Scenarios")
@Owner("Mohamed Kamal")
public class PersonaE2ETest extends BaseTest {

    /**
     * TC_E2E_004 - Persona users (problem, performance_glitch, error, visual)
     * can log in and perform basic navigation (Inventory → Cart → Inventory).
     */
    @Test(
            description = "Persona users (problem/performance_glitch/error/visual) can complete a basic purchase flow",
            dataProvider = "personaUsersWithItemAndCheckout",
            dataProviderClass = E2EDataProvider.class,
            groups = {"regression", "e2e", "persona", "checkout"}
    )
    @Story("Persona users can log in, add an item to cart, and complete checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description(
            "Verifies that each persona user (problem_user, performance_glitch_user, error_user, visual_user) " +
                    "can log in, add a single item to the cart, go through checkout steps, and reach the Checkout Complete page."
    )
    public void TC_E2E_004_personaUsersBasicNavigation(UserModel user, ItemModel item, CheckoutModel checkoutData) {

        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();
        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();
        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();
        CheckoutCompletePage completePage = new CheckoutCompletePage();

        // 1) Login as persona user
        loginPage.loginAs(user.getUsername(), user.getPassword());
        boolean onInventory = loginPage.waitUntilOnInventoryPage();

        Assert.assertTrue(
                onInventory,
                "Persona user '" + user.getScenarioId() + "' should reach Inventory page."
        );

        // 2) Add one item to cart
        inventoryPage.addItemToCartById(item.getItemId());
        int cartBadgeCount = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(
                cartBadgeCount,
                1,
                "Cart badge should be 1 after adding one item for persona user '" + user.getScenarioId() + "'."
        );

        // 3) Open cart and verify item is present
        cartPage.openCartFromHeader();
        Assert.assertTrue(
                cartPage.isOnCartPage(),
                "Persona user should be able to open Cart page."
        );

        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Item '" + item.getItemName() +
                        "' should be present in Cart for persona user '" + user.getScenarioId() + "'."
        );

        // 4) Start checkout (Step One)
        cartPage.clickCheckout();

        Assert.assertTrue(
                stepOne.isOnCheckoutStepOnePage(),
                "Persona user '" + user.getScenarioId() +
                        "' should be on Checkout Step One page after clicking Checkout."
        );

        // 5) Fill user info from checkout_valid.json
        stepOne.fillUserInformation(
                checkoutData.getFirstName(),
                checkoutData.getLastName(),
                checkoutData.getPostalCode()
        );
        stepOne.clickContinue();

        // 6) Finish and verify complete page
        stepTwo.clickFinish();

        Assert.assertTrue(
                completePage.isOnCheckoutCompletePage(),
                "Persona user '" + user.getScenarioId() +
                        "' should be on Checkout Complete page after finishing checkout."
        );

        String header = completePage.getCompleteHeaderText();
        Assert.assertTrue(
                header.toLowerCase().contains("thank you"),
                "Complete header should contain 'Thank you' for persona user '" +
                        user.getScenarioId() + "'. Actual: " + header
        );
    }
}
