package com.saucedemo.tests.checkout;

import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.tests.base.CheckoutBaseTest;
import com.saucedemo.tests.utils.dataProviders.CheckoutDataProvider;
import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Checkout - Step Three: Order Completion")
@Owner("Mohamed Kamal")
public class CheckoutCompleteTests extends CheckoutBaseTest {


    // ---------- Tests ----------

    /**
     * TC_CC_001 - Checkout Complete page shows thank-you header and message.
     */
    @Test(
            description = "Display thank-you header and confirmation message on Checkout Complete page",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"smoke", "regression", "checkout"}
    )
    @Story("View order completion confirmation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After a successful checkout, verifies that the Checkout Complete page " +
            "shows a 'Thank you' header and a non-empty confirmation message.")
    public void TC_CC_001_completePageShowsThankYou(CheckoutModel checkoutData, ItemModel item) {

        // Precondition: full checkout done, user is on Checkout Complete page
        goToCheckoutComplete(checkoutData, item);

        CheckoutCompletePage completePage = new CheckoutCompletePage();

        String header  = completePage.getCompleteHeaderText();
        String message = completePage.getCompleteMessageText();

        Assert.assertTrue(
                header.toLowerCase().contains("thank you"),
                "Complete header should contain 'Thank you'. Actual: " + header
        );

        Assert.assertFalse(
                message.isBlank(),
                "Complete message text should not be empty."
        );
    }

    /**
     * TC_CC_002 - Back Home from complete page navigates back to Inventory page.
     */
    @Test(
            description = "Use Back Home on Checkout Complete page to return to the Inventory page",
            dataProvider = "validCheckoutDataWithItem",
            dataProviderClass = CheckoutDataProvider.class,
            groups = {"regression", "checkout", "navigation"}
    )
    @Story("Navigate back to inventory after order completion")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that clicking Back Home on the Checkout Complete page " +
            "navigates the user back to the Inventory page.")
    public void TC_CC_002_backHomeNavigatesToInventory(CheckoutModel checkoutData, ItemModel item) {

        // Precondition: full checkout done, user is on Checkout Complete page
        goToCheckoutComplete(checkoutData, item);

        CheckoutCompletePage completePage = new CheckoutCompletePage();
        InventoryPage inventoryPage = new InventoryPage();

        completePage.clickBackHome();

        Assert.assertTrue(
                inventoryPage.isOnInventoryPage(),
                "After clicking Back Home, user should be back on Inventory page."
        );
    }

}
