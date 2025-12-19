package com.saucedemo.tests.base;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;


@Epic("SauceDemo Web Application")
@Feature("Checkout – Base Navigation Helpers")
@Owner("Mohamed Kamal")
public class CheckoutBaseTest extends LoginBaseTest {

    @Step("Go to Checkout Step One with item: {item.itemId}")
    protected void goToCheckoutStepOne(ItemModel item) {
        InventoryPage inventoryPage = new InventoryPage();
        CartPage cartPage = new CartPage();

        inventoryPage.addItemToCartById(item.getItemId());
        cartPage.openCartFromHeader();
        cartPage.clickCheckout();
        // Now the user should be on Checkout Step One page
    }

    @Step("Go to Checkout Step Two with item: {item.itemId} and checkout data")
    protected void goToCheckoutStepTwo(CheckoutModel checkoutData, ItemModel item) {

        // 1) Navigate to Checkout Step One
        goToCheckoutStepOne(item);

        // 2) Interact with Checkout Step One page
        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();
        stepOne.fillUserInformation(
                checkoutData.getFirstName(),
                checkoutData.getLastName(),
                checkoutData.getPostalCode()
        );
        stepOne.clickContinue();
        // Now the user should be on Checkout Step Two page
    }

    @Step("Go to Checkout Complete page with item: {item.itemId} and checkout data")
    protected void goToCheckoutComplete(CheckoutModel checkoutData, ItemModel item) {

        // 1) Navigate to Checkout Step Two
        goToCheckoutStepTwo(checkoutData, item);

        // 2) Finish checkout at Step Two
        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();
        stepTwo.clickFinish();
        // Now the user should be on Checkout Complete page
    }
}
