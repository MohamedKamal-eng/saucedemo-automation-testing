package com.saucedemo.tests.product;

import com.saucedemo.pageComponents.CartItemComponent;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.ProductDetailsPage;
import com.saucedemo.tests.base.LoginBaseTest;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Product Details – Item information & actions")
@Owner("Mohamed Kamal")
public class ProductDetailsTests extends LoginBaseTest {

    /**
     * TC_PD_001 - Verify product details page shows correct product name.
     */
    @Test(
            description = "Product details page shows the correct product name",
            groups = {"product", "regression"}
    )
    @Story("Product Details – Display item information")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that when navigating from Inventory to the product details page, the product name matches the item selected from test data.")
    public void TC_PD_001_productDetailsShowCorrectName(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        ProductDetailsPage detailsPage = new ProductDetailsPage();

        // Navigate to product details from inventory by clicking the item name
        inventoryPage.openItemDetailsByName(item.getItemName());

        String detailsName = detailsPage.getProductName();

        Assert.assertEquals(
                detailsName,
                item.getItemName(),
                "Product name on details page should match the expected item name from test data."
        );
    }

    /**
     * TC_PD_002 - Add item to cart from product details page and verify it appears in cart.
     */
    @Test(
            description = "Add item to cart from product details page and verify it appears in the cart",
            groups = {"product", "regression"}
    )
    @Story("Product Details – Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that adding an item to the cart from the product details page results in the item being displayed in the cart.")
    public void TC_PD_002_addItemToCartFromDetails(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        ProductDetailsPage detailsPage = new ProductDetailsPage();
        CartPage cartPage = new CartPage();

        // Navigate to product details
        inventoryPage.openItemDetailsByName(item.getItemName());

        // Add item to cart from details page
        detailsPage.addItemToCartById(item.getItemId());

        // Open cart and verify item is present
        cartPage.openCartFromHeader();

        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Item added from product details page should appear in the cart."
        );
    }

    /**
     * TC_PD_003 - Remove item from cart from product details page and verify cart is updated.
     */
    @Test(
            description = "Remove item from cart using product details page and verify it is removed from the cart",
            groups = {"product", "regression"}
    )
    @Story("Product Details – Remove from cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that removing an item from the cart via the product details page correctly updates the cart so the item is no longer present.")
    public void TC_PD_003_removeItemFromDetails(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        ProductDetailsPage detailsPage = new ProductDetailsPage();
        CartPage cartPage = new CartPage();

        // Precondition: add item to cart from details page
        inventoryPage.openItemDetailsByName(item.getItemName());
        detailsPage.addItemToCartById(item.getItemId());

        // Confirm in cart first
        cartPage.openCartFromHeader();
        Assert.assertTrue(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Precondition failed: item should be present in cart before removal."
        );

        // Go back to product details page (from inventory)
        cartPage.clickContinueShopping();
        inventoryPage.openItemDetailsByName(item.getItemName());

        // Remove from cart using details page button
        detailsPage.removeItemFromCartById(item.getItemId());

        // Verify in cart that item is no longer present
        cartPage.openCartFromHeader();

        Assert.assertFalse(
                CartItemComponent.isItemPresentByName(item.getItemName()),
                "Item removed from product details page should no longer appear in the cart."
        );
    }

    /**
     * TC_PD_004 - Back to products button should navigate back to Inventory page.
     */
    @Test(
            description = "Back to products button navigates back to Inventory page",
            groups = {"product", "smoke", "regression"}
    )
    @Story("Product Details – Navigation back to Inventory")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that clicking 'Back to products' on the product details page returns the user to the Inventory page.")
    public void TC_PD_004_backToProductsNavigatesToInventory(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        ProductDetailsPage detailsPage = new ProductDetailsPage();

        // Navigate to product details
        inventoryPage.openItemDetailsByName(item.getItemName());

        // Click Back to products
        detailsPage.clickBackToProducts();

        Assert.assertTrue(
                inventoryPage.isOnInventoryPage(),
                "After clicking Back to products, user should be back on Inventory page"
        );
    }
}
