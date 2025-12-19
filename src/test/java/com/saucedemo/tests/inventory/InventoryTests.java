package com.saucedemo.tests.inventory;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.ProductDetailsPage;
import com.saucedemo.tests.base.LoginBaseTest;
import com.saucedemo.tests.utils.dataProviders.InventoryDataProvider;
import com.saucedemo.tests.utils.models.ItemModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Epic("SauceDemo Web Application")
@Feature("Inventory – Product Listing, Sorting & Social Links")
@Owner("Mohamed Kamal")
public class InventoryTests extends LoginBaseTest {

    // -------------------- Inventory Tests --------------------

    /**
     * TC_INV_001 - Add single item to cart from Inventory page and verify cart badge is updated.
     */
    @Test(
            description = "Add a single item to the cart from Inventory and verify the cart badge increments by one",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"regression", "inventory", "cart"}
    )
    @Story("Add items to cart from Inventory")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adds a single item to the cart from the Inventory page and asserts that the cart badge increases by 1.")
    public void TC_INV_001_addSingleItemToCart(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();

        int initialCount = inventoryPage.getCartBadgeCount();
        inventoryPage.addItemToCartById(item.getItemId());
        int finalCount = inventoryPage.getCartBadgeCount();

        Assert.assertEquals(
                finalCount,
                initialCount + 1,
                "Cart badge count should increase by 1 after adding an item."
        );
    }

    /**
     * TC_INV_002 - Remove item from cart using Inventory page and verify cart badge decreases.
     */
    @Test(
            description = "Remove an item from the cart using Inventory and verify the cart badge decreases",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"regression", "inventory", "cart"}
    )
    @Story("Remove items from cart via Inventory")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that removing an item from the cart using the Inventory page decreases the cart badge count appropriately.")
    public void TC_INV_002_removeItemFromCart(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();

        // Ensure item is added first
        inventoryPage.addItemToCartById(item.getItemId());
        int countAfterAdd = inventoryPage.getCartBadgeCount();

        inventoryPage.removeItemFromCartById(item.getItemId());
        int countAfterRemove = inventoryPage.getCartBadgeCount();

        Assert.assertEquals(
                countAfterRemove,
                Math.max(0, countAfterAdd - 1),
                "Cart badge count should decrease by 1 after removing an item (or go to 0)."
        );
    }

    /**
     * TC_INV_003 - Navigate to Product Details by clicking item name.
     */
    @Test(
            description = "Open product details by clicking the item name on Inventory",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"regression", "inventory", "navigation"}
    )
    @Story("Navigate to product details from Inventory")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicking on an item name on the Inventory page should navigate to the product details page.")
    public void TC_INV_003_openDetailsByName(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        ProductDetailsPage productDetailsPage = new ProductDetailsPage();

        inventoryPage.openItemDetailsByName(item.getItemName());

        Assert.assertTrue(
                productDetailsPage.isOnProductDetailsPage(),
                "After clicking item name, user should be on Product Details page."
        );
    }

    /**
     * TC_INV_004 - Navigate to Product Details by clicking item image.
     */
    @Test(
            description = "Open product details by clicking the item image on Inventory",
            dataProvider = "singleInventoryItem",
            dataProviderClass = InventoryDataProvider.class,
            groups = {"regression", "inventory", "navigation"}
    )
    @Story("Navigate to product details from Inventory")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicking on an item image on the Inventory page should navigate to the product details page.")
    public void TC_INV_004_openDetailsByImage(ItemModel item) {

        InventoryPage inventoryPage = new InventoryPage();
        ProductDetailsPage productDetailsPage = new ProductDetailsPage();

        inventoryPage.openItemDetailsByImage(item.getItemName());

        Assert.assertTrue(
                productDetailsPage.isOnProductDetailsPage(),
                "After clicking item image, user should be on Product Details page."
        );
    }

    /**
     * TC_INV_005 - Sort items by Name (A to Z) and verify order is ascending.
     */
    @Test(
            description = "Sort items by name A to Z and verify ascending order",
            groups = {"regression", "inventory", "sorting"}
    )
    @Story("Sort inventory items by name")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC_INV_005")
    @Description("Applies the Name (A to Z) sort on Inventory and verifies that the displayed list is in ascending order.")
    public void TC_INV_005_sortByNameAToZ() {

        InventoryPage inventoryPage = new InventoryPage();

        inventoryPage.sortByNameAToZ();
        List<String> uiNames = inventoryPage.getAllItemNames();

        List<String> sortedNames = new ArrayList<>(uiNames);
        Collections.sort(sortedNames);

        Assert.assertEquals(
                uiNames,
                sortedNames,
                "Item names should be sorted alphabetically A to Z."
        );
    }

    /**
     * TC_INV_006 - Sort items by Name (Z to A) and verify order is descending.
     */
    @Test(
            description = "Sort items by name Z to A and verify descending order",
            groups = {"regression", "inventory", "sorting"}
    )
    @Story("Sort inventory items by name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Applies the Name (Z to A) sort on Inventory and verifies that the displayed list is in descending order.")
    public void TC_INV_006_sortByNameZToA() {

        InventoryPage inventoryPage = new InventoryPage();

        inventoryPage.sortByNameZToA();
        List<String> uiNames = inventoryPage.getAllItemNames();

        List<String> sortedNames = new ArrayList<>(uiNames);
        Collections.sort(sortedNames);
        Collections.reverse(sortedNames);

        Assert.assertEquals(
                uiNames,
                sortedNames,
                "Item names should be sorted alphabetically Z to A."
        );
    }

    /**
     * TC_INV_007 - Sort items by Price (low to high) and verify ascending order.
     */
    @Test(
            description = "Sort items by price from low to high and verify ascending order",
            groups = {"regression", "inventory", "sorting"}
    )
    @Story("Sort inventory items by price")
    @Severity(SeverityLevel.NORMAL)
    @Description("Applies the Price (low to high) sort on Inventory and verifies that the displayed prices are in ascending order.")
    public void TC_INV_007_sortByPriceLowToHigh() {

        InventoryPage inventoryPage = new InventoryPage();

        inventoryPage.sortByPriceLowToHigh();
        List<Double> uiPrices = inventoryPage.getAllItemPrices();

        List<Double> sortedPrices = new ArrayList<>(uiPrices);
        Collections.sort(sortedPrices);

        Assert.assertEquals(
                uiPrices,
                sortedPrices,
                "Item prices should be sorted from low to high."
        );
    }

    /**
     * TC_INV_008 - Sort items by Price (high to low) and verify descending order.
     */
    @Test(
            description = "Sort items by price from high to low and verify descending order",
            groups = {"regression", "inventory", "sorting"}
    )
    @Story("Sort inventory items by price")
    @Severity(SeverityLevel.NORMAL)
    @Description("Applies the Price (high to low) sort on Inventory and verifies that the displayed prices are in descending order.")
    public void TC_INV_008_sortByPriceHighToLow() {

        InventoryPage inventoryPage = new InventoryPage();

        inventoryPage.sortByPriceHighToLow();
        List<Double> uiPrices = inventoryPage.getAllItemPrices();

        List<Double> sortedPrices = new ArrayList<>(uiPrices);
        Collections.sort(sortedPrices);
        Collections.reverse(sortedPrices);

        Assert.assertEquals(
                uiPrices,
                sortedPrices,
                "Item prices should be sorted from high to low."
        );
    }

    /**
     * TC_INV_009 - Verify Twitter icon opens Twitter URL in a new tab/window.
     */
    @Test(
            description = "Verify Twitter social icon opens the correct URL in a new tab",
            groups = {"regression", "inventory", "social", "external_links"}
    )
    @Story("Open social links from Inventory footer")
    @Severity(SeverityLevel.MINOR)
    @Description("Clicks the Twitter icon in the Inventory footer and verifies that a new tab opens with the expected Twitter URL prefix.")
    public void TC_INV_009_twitterIconOpensCorrectUrl() {

        InventoryPage inventoryPage = new InventoryPage();

        String actualUrl      = inventoryPage.openTwitterAndGetTargetUrl();
        String expectedUrlStart = inventoryPage.getExpectedTwitterUrlPrefix();

        Assert.assertTrue(
                actualUrl.startsWith(expectedUrlStart),
                "Twitter URL should start with expected value. Expected start: " +
                        expectedUrlStart + ", actual: " + actualUrl
        );
    }

    /**
     * TC_INV_010 - Verify Facebook icon opens Facebook URL in a new tab/window.
     */
    @Test(
            description = "Verify Facebook social icon opens the correct URL in a new tab",
            groups = {"regression", "inventory", "social", "external_links"}
    )
    @Story("Open social links from Inventory footer")
    @Severity(SeverityLevel.MINOR)
    @Description("Clicks the Facebook icon in the Inventory footer and verifies that a new tab opens with the expected Facebook URL prefix.")
    public void TC_INV_010_facebookIconOpensCorrectUrl() {

        InventoryPage inventoryPage = new InventoryPage();

        String actualUrl        = inventoryPage.openFacebookAndGetTargetUrl();
        String expectedUrlStart = inventoryPage.getExpectedFacebookUrlPrefix();

        Assert.assertTrue(
                actualUrl.startsWith(expectedUrlStart),
                "Facebook URL should start with expected value. Expected start: " +
                        expectedUrlStart + ", actual: " + actualUrl
        );
    }

    /**
     * TC_INV_011 - Verify LinkedIn icon opens LinkedIn URL in a new tab/window.
     */
    @Test(
            description = "Verify LinkedIn social icon opens the correct URL in a new tab",
            groups = {"regression", "inventory", "social", "external_links"}
    )
    @Story("Open social links from Inventory footer")
    @Severity(SeverityLevel.MINOR)
    @Description("Clicks the LinkedIn icon in the Inventory footer and verifies that a new tab opens with the expected LinkedIn URL prefix.")
    public void TC_INV_011_linkedInIconOpensCorrectUrl() {

        InventoryPage inventoryPage = new InventoryPage();

        String actualUrl        = inventoryPage.openLinkedInAndGetTargetUrl();
        String expectedUrlStart = inventoryPage.getExpectedLinkedInUrlPrefix();

        Assert.assertTrue(
                actualUrl.startsWith(expectedUrlStart),
                "LinkedIn URL should start with expected value. Expected start: " +
                        expectedUrlStart + ", actual: " + actualUrl
        );
    }
}
