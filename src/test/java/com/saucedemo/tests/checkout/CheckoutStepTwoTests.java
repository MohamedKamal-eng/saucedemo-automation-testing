package com.saucedemo.tests.checkout;

import com.saucedemo.pageComponents.CartItemComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.tests.base.CheckoutBaseTest;
import com.saucedemo.tests.utils.dataProviders.CheckoutDataProvider;
import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Checkout - Step Two: Order Overview")
@Owner("Mohamed Kamal")
public class CheckoutStepTwoTests extends CheckoutBaseTest {


    // ---------- Tests ----------

    /**
     * TC_CO2_001 - Checkout overview shows the item that was added to cart.
     */
    @Test(
            description = "Display all items added to cart in the Checkout Step Two overview",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout"}
    )
    @Story("View order items in overview")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the items added to the cart are correctly displayed " +
            "in the Checkout Step Two (Overview) item list.")
    public void TC_CO2_001_overviewShowsCartItems(CheckoutModel checkoutData, ItemModel item) {

        // Precondition: user on Checkout Step Two with the given item in the cart
        goToCheckoutStepTwo(checkoutData, item);

        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();

        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Item '" + item.getItemName() + "' should appear in Checkout Step Two overview."
        );

        Assert.assertTrue(
                stepTwo.getItemsCount() >= 1,
                "There should be at least one item in Checkout Step Two overview."
        );
    }
    /**
     * TC_CO2_002 - Cancel from Step Two navigates back to cart page.
     */
    @Test(
            description = "Use Cancel on Checkout Step Two to return back to the cart page",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout", "navigation"}
    )
    @Story("Cancel checkout on Step Two")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that when the user clicks Cancel on Checkout Step Two, they are navigated " +
            "back to the cart page instead of completing the checkout.")
    public void TC_CO2_002_cancelNavigatesBackToCart(CheckoutModel checkoutData, ItemModel item) {

        goToCheckoutStepTwo(checkoutData, item);

        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();
        CartPage cartPage = new CartPage();

        stepTwo.clickCancel();

        Assert.assertTrue(
                cartPage.isOnCartPage(),
                "After cancelling on Step Two, user should be back on Cart page."
        );
    }

    /**
     * TC_CO2_003 - Finish from Step Two navigates to Checkout Complete page.
     */
    @Test(
            description = "Finish checkout on Step Two and navigate to the Checkout Complete page",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"smoke", "regression", "checkout"}
    )
    @Story("Complete checkout from Step Two")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that clicking Finish on Checkout Step Two successfully completes the order " +
            "and navigates the user to the Checkout Complete page.")
    public void TC_CO2_003_finishNavigatesToCompletePage(CheckoutModel checkoutData, ItemModel item) {

        goToCheckoutStepTwo(checkoutData, item);

        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();
        CheckoutCompletePage completePage = new CheckoutCompletePage();

        stepTwo.clickFinish();

        Assert.assertTrue(
                completePage.isOnCheckoutCompletePage(),
                "After clicking Finish on Step Two, user should be on Checkout Complete page."
        );
    }


    /**
     * TC_CO2_004 - Verify Payment and Shipping information are displayed on Step Two.
     */
    @Test(
            description = "Display payment and shipping information in Checkout Step Two summary",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout"}
    )
    @Story("View payment and shipping information")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that the payment method and shipping information sections are present " +
            "and populated on Checkout Step Two.")
    public void TC_CO2_004_paymentAndShippingInfoAreDisplayed(CheckoutModel checkoutData, ItemModel item) {

        goToCheckoutStepTwo(checkoutData, item);

        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();

        String paymentInfo = stepTwo.getPaymentInformation();
        String shippingInfo = stepTwo.getShippingInformation();

        Assert.assertNotNull(paymentInfo, "Payment information should not be null on Step Two.");
        Assert.assertFalse(paymentInfo.isBlank(), "Payment information should not be empty on Step Two.");

        Assert.assertNotNull(shippingInfo, "Shipping information should not be null on Step Two.");
        Assert.assertFalse(shippingInfo.isBlank(), "Shipping information should not be empty on Step Two.");
    }


    /**
     * TC_CO2_005 - Verify that Total equals Item total + Tax on Step Two.
     */
    @Test(
            description = "Calculate total amount on Checkout Step Two as Item total plus tax",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout"}
    )
    @Story("Verify order pricing and totals")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the Total amount displayed on Checkout Step Two equals the sum " +
            "of the Item total and Tax values.")
    public void TC_CO2_005_totalEqualsItemTotalPlusTax(CheckoutModel checkoutData, ItemModel item) {

        goToCheckoutStepTwo(checkoutData, item);

        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();

        double itemTotal = stepTwo.getItemTotalAmount();
        double tax       = stepTwo.getTaxAmount();
        double total     = stepTwo.getTotalAmount();

        double expected = itemTotal + tax;

        Assert.assertEquals(
                total,
                expected,
                "Total amount on Step Two should equal Item total + Tax."
        );
    }

}
