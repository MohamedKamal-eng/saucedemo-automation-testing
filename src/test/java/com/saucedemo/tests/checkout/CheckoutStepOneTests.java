package com.saucedemo.tests.checkout;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.tests.base.CheckoutBaseTest;
import com.saucedemo.tests.utils.dataProviders.CheckoutDataProvider;
import com.saucedemo.tests.utils.models.CheckoutModel;

import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Checkout - Step One: Customer Information")
@Owner("Mohamed Kamal")
public class CheckoutStepOneTests extends CheckoutBaseTest {

    /**
     * TC_CO1_001 - Valid checkout data on Step One navigates to Step Two.
     */
    @Test(
            description = "Navigate from Checkout Step One to Step Two using valid customer information",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"smoke", "regression", "checkout"}
    )
    @Story("Complete customer information on Step One")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that when the user fills valid first name, last name, and postal code " +
            "on Checkout Step One, they are successfully navigated to Checkout Step Two.")
    public void TC_CO1_001_validCheckoutNavigatesToStepTwo(CheckoutModel data, ItemModel item) {

        // 1) Precondition: we are on Checkout Step One with an item in the cart
        goToCheckoutStepOne(item);

        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();
        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage();

        Assert.assertTrue(
                stepOne.isOnCheckoutStepOnePage(),
                "Precondition failed: user is not on Checkout Step One page."
        );

        // 2) Fill valid customer information
        stepOne.fillUserInformation(
                data.getFirstName(),
                data.getLastName(),
                data.getPostalCode()
        );

        // 3) Continue to Step Two
        stepOne.clickContinue();

        Assert.assertTrue(
                stepTwo.isOnCheckoutStepTwoPage(),
                "After valid checkout data, user should navigate to Checkout Step Two page."
        );
    }

    /**
     * TC_CO1_002 - Missing first name shows appropriate error and stays on Step One.
     */
    @Test(
            description = "Show error message when first name is missing on Checkout Step One",
            dataProvider = "missingFirstNameWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout", "negative"}
    )
    @Story("Validate mandatory customer fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that when the first name is missing while submitting Checkout Step One, " +
            "an appropriate error message is shown and the user remains on the same step.")
    public void TC_CO1_002_missingFirstNameShowsError(CheckoutModel data, ItemModel item) {

        // Precondition: user is on Checkout Step One with an item in the cart
        goToCheckoutStepOne(item);

        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();

        Assert.assertTrue(
                stepOne.isOnCheckoutStepOnePage(),
                "Precondition failed: user is not on Checkout Step One page."
        );

        stepOne.fillUserInformation(
                data.getFirstName(),   // this will be null/empty according to your JSON
                data.getLastName(),
                data.getPostalCode()
        );

        stepOne.clickContinue();

        Assert.assertTrue(
                stepOne.isErrorDisplayed(),
                "Error message should be displayed when first name is missing."
        );
        Assert.assertEquals(
                stepOne.getErrorMessageText(),
                data.getExpectedErrorMessage(),
                "Error message text for missing first name does not match expected."
        );
    }

    /**
     * TC_CO1_003 - Missing last name shows appropriate error and stays on Step One.
     */
    @Test(
            description = "Show error message when last name is missing on Checkout Step One",
            dataProvider = "missingLastNameWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout", "negative"}
    )
    @Story("Validate mandatory customer fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that when the last name is missing while submitting Checkout Step One, " +
            "an appropriate error message is shown and the user remains on the same step.")
    public void TC_CO1_003_missingLastNameShowsError(CheckoutModel data, ItemModel item) {

        // Precondition: user is on Checkout Step One with an item in the cart
        goToCheckoutStepOne(item);

        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();

        Assert.assertTrue(
                stepOne.isOnCheckoutStepOnePage(),
                "Precondition failed: user is not on Checkout Step One page."
        );

        stepOne.fillUserInformation(
                data.getFirstName(),
                data.getLastName(),      // missing/invalid according to JSON
                data.getPostalCode()
        );
        stepOne.clickContinue();

        Assert.assertTrue(
                stepOne.isErrorDisplayed(),
                "Error message should be displayed when last name is missing."
        );
        Assert.assertEquals(
                stepOne.getErrorMessageText(),
                data.getExpectedErrorMessage(),
                "Error message text for missing last name does not match expected."
        );
    }

    /**
     * TC_CO1_004 - Missing postal code shows appropriate error and stays on Step One.
     */
    @Test(
            description = "Show error message when postal code is missing on Checkout Step One",
            dataProvider = "missingPostalCodeWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout", "negative"}
    )
    @Story("Validate mandatory customer fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that when the postal code is missing while submitting Checkout Step One, " +
            "an appropriate error message is shown and the user remains on the same step.")
    public void TC_CO1_004_missingPostalCodeShowsError(CheckoutModel data, ItemModel item) {

        // Precondition: user is on Checkout Step One with an item in the cart
        goToCheckoutStepOne(item);

        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();

        Assert.assertTrue(
                stepOne.isOnCheckoutStepOnePage(),
                "Precondition failed: user is not on Checkout Step One page."
        );

        stepOne.fillUserInformation(
                data.getFirstName(),
                data.getLastName(),
                data.getPostalCode()     // missing/invalid according to JSON
        );
        stepOne.clickContinue();

        Assert.assertTrue(
                stepOne.isErrorDisplayed(),
                "Error message should be displayed when postal code is missing."
        );
        Assert.assertEquals(
                stepOne.getErrorMessageText(),
                data.getExpectedErrorMessage(),
                "Error message text for missing postal code does not match expected."
        );
    }

    /**
     * TC_CO1_005 - Verify Cancel button on Checkout Step One returns to Cart
     *              and does not persist entered customer info.
     */
    @Test(
            description = "Use Cancel on Checkout Step One to return to cart and verify customer data is cleared",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout", "navigation"}
    )
    @Story("Cancel checkout on Step One")
    @Severity(SeverityLevel.MINOR)
    @Description("Fills in valid customer information on Checkout Step One, clicks Cancel to return to the Cart page, " +
            "then re-opens checkout to verify that the customer fields are cleared and not persisted.")
    public void TC_CO1_005_cancelReturnsToCartAndDoesNotPersistInfo(CheckoutModel data, ItemModel item) {

        // Precondition: 1 item in cart, user on Checkout Step One
        goToCheckoutStepOne(item);

        CheckoutStepOnePage stepOne = new CheckoutStepOnePage();
        CartPage cartPage = new CartPage();

        Assert.assertTrue(
                stepOne.isOnCheckoutStepOnePage(),
                "Precondition failed: user is not on Checkout Step One page."
        );

        // Fill customer info
        stepOne.fillUserInformation(
                data.getFirstName(),
                data.getLastName(),
                data.getPostalCode()
        );

        // Click Cancel -> should go back to Cart
        stepOne.clickCancel();

        Assert.assertTrue(
                cartPage.isOnCartPage(),
                "After clicking Cancel, user should be returned to the Cart page."
        );

        // From Cart, go to Checkout again
        cartPage.clickCheckout();

        CheckoutStepOnePage stepOneAgain = new CheckoutStepOnePage();

        Assert.assertTrue(
                stepOneAgain.isOnCheckoutStepOnePage(),
                "User should be back on Checkout Step One page after re-opening checkout."
        );

        // Verify info is not persisted
        Assert.assertEquals(
                stepOneAgain.getFirstNameValue(),
                "",
                "First name should not be persisted after cancelling checkout."
        );
        Assert.assertEquals(
                stepOneAgain.getLastNameValue(),
                "",
                "Last name should not be persisted after cancelling checkout."
        );
        Assert.assertEquals(
                stepOneAgain.getPostalCodeValue(),
                "",
                "Postal code should not be persisted after cancelling checkout."
        );
    }
}
