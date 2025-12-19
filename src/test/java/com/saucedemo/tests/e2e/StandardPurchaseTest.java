package com.saucedemo.tests.e2e;

import com.saucedemo.pageComponents.CartItemComponent;
import com.saucedemo.pages.*;
import com.saucedemo.tests.base.BaseTest;
import com.saucedemo.tests.utils.dataProviders.E2EDataProvider;
import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.tests.utils.models.OrderModel;
import com.saucedemo.tests.utils.models.UserModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("SauceDemo Web Application")
@Feature("E2E – Purchase Flow")
@Owner("Mohamed Kamal")
public class StandardPurchaseTest extends BaseTest {

    /**
     * TC_E2E_001 - Standard user completes purchase successfully using order data from JSON.
     */
    @Test(
            description = "Standard user completes a full purchase flow using JSON-based order data",
            dataProvider = "standardSingleItemOrderFull",
            dataProviderClass = E2EDataProvider.class,
            groups = {"regression", "e2e", "checkout", "cart", "inventory"}
    )
    @Story("Standard user completes a single-item purchase")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC_E2E_001")
    @Description(
            "Logs in as the standard user, adds all items from a JSON-defined order to the cart, " +
                    "verifies them in Cart and Checkout Overview, then completes the purchase and checks " +
                    "that the Checkout Complete page is displayed with a thank-you message."
    )
    public void TC_E2E_001_standardUserCompletesPurchase(OrderModel order, UserModel standardUser, CheckoutModel checkoutData, List<ItemModel> orderItems) {

        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();
        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();
        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();
        CheckoutCompletePage completePage = new CheckoutCompletePage();
        LoginPage loginPage = new LoginPage();

        // 0) Login as standard user
        loginPage.loginAs(standardUser.getUsername(), standardUser.getPassword());
        Assert.assertTrue(
                loginPage.waitUntilOnInventoryPage(),
                "User should reach Inventory page after login."
        );

        // 1) Add all items from order to cart
        for (ItemModel item : orderItems) {
            inventoryPage.addItemToCartById(item.getItemId());
        }

        // 2) Verify items in cart
        cartPage.openCartFromHeader();
        for (ItemModel item : orderItems) {
            Assert.assertTrue(
                    CartItemComponent.isItemPresentByName(item.getItemName()),
                    "Item '" + item.getItemName() + "' should be present in Cart."
            );
        }

        // 3) Start checkout
        cartPage.clickCheckout();

        Assert.assertTrue(
                stepOne.isOnCheckoutStepOnePage(),
                "User should be on Checkout Step One page."
        );

        // 4) Fill user info from checkout_valid.json
        stepOne.fillUserInformation(
                checkoutData.getFirstName(),
                checkoutData.getLastName(),
                checkoutData.getPostalCode()
        );
        stepOne.clickContinue();

        Assert.assertTrue(
                stepTwo.isOnCheckoutStepTwoPage(),
                "User should be on Checkout Step Two page after valid Step One info."
        );

        // 5) Verify items appear in overview
        for (ItemModel item : orderItems) {
            Assert.assertTrue(
                    CartItemComponent.isItemPresentByName(item.getItemName()),
                    "Item '" + item.getItemName() + "' should be present in Checkout overview."
            );
        }

        // 6) Finish and verify complete page
        stepTwo.clickFinish();

        Assert.assertTrue(
                completePage.isOnCheckoutCompletePage(),
                "User should be on Checkout Complete page after finishing checkout."
        );

        String header = completePage.getCompleteHeaderText();
        Assert.assertTrue(
                header.toLowerCase().contains("thank you"),
                "Complete header should contain 'Thank you'. Actual: " + header
        );
    }
}
