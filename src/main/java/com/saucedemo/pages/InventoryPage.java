package com.saucedemo.pages;

import com.saucedemo.utilities.config.PropertiesUtils;
import com.saucedemo.utilities.selenium.driver.DriverManager;
import com.saucedemo.utilities.selenium.helperClasses.BrowserUtils;
import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import com.saucedemo.utilities.selenium.helperClasses.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {

    // Locators for listing
    private final By inventoryItemContainer = By.className("inventory_item");
    private final By inventoryItemImgContainer = By.className("inventory_item_img");
    private final By itemNameLocator = By.className("inventory_item_name");
    private final By itemPriceLocator = By.className("inventory_item_price");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    // ✅ Correct locator for sort dropdown (class = product_sort_container)
    private final By sortDropdown = By.className("product_sort_container");

    // Social icons
    private final By twitterIcon = By.cssSelector("li.social_twitter a");
    private final By facebookIcon = By.cssSelector("li.social_facebook a");
    private final By linkedinIcon = By.cssSelector("li.social_linkedin a");

    // ---------- Private helpers for dynamic locators (by itemId) ----------

    /**
     * Uses itemId from JSON, e.g. "sauce-labs-backpack".
     * In DOM: button[data-test='add-to-cart-sauce-labs-backpack']
     */
    private By addToCartButtonByItemId(String itemId) {
        return By.cssSelector("button[data-test='add-to-cart-" + itemId + "']");
    }

    /**
     * After adding, the button becomes a remove button:
     * button[data-test='remove-sauce-labs-backpack']
     */
    private By removeButtonByItemId(String itemId) {
        return By.cssSelector("button[data-test='remove-" + itemId + "']");
    }

    // ---------- Actions on inventory items ----------

    public void addItemToCartById(String itemId) {
        ElementActions.click(addToCartButtonByItemId(itemId));
    }

    public void removeItemFromCartById(String itemId) {
        ElementActions.click(removeButtonByItemId(itemId));
    }

    /**
     * Open product details by clicking the *name* of the item.
     */
    public void openItemDetailsByName(String itemName) {
        // Ensure items are visible
        WaitHelpers.waitForVisibility(inventoryItemContainer);

        List<WebElement> items = driver.findElements(inventoryItemContainer);
        for (WebElement item : items) {

            String actualName = ElementActions.getText(item,itemNameLocator);
            if (actualName.equals(itemName)) {
                ElementActions.click(item,itemNameLocator);
                return;
            }
        }
        throw new IllegalStateException("Item with name '" + itemName + "' not found on Inventory page.");
    }

    /**
     * Open product details by clicking the *image* of the item.
     */
    public void openItemDetailsByImage(String itemName) {
        WaitHelpers.waitForVisibility(inventoryItemContainer);

        List<WebElement> items = driver.findElements(inventoryItemContainer);
        for (WebElement item : items) {

            String actualName = ElementActions.getText(item,itemNameLocator);
            if (actualName.equals(itemName)) {
                ElementActions.click(item,inventoryItemImgContainer);
                return;
            }
        }
        throw new IllegalStateException("Item with name '" + itemName + "' not found on Inventory page.");
    }

    // ---------- Cart badge & navigation ----------

    public int getCartBadgeCount() {
        if (!ElementActions.isDisplayed(cartBadge)) {
            return 0;
        }
        String text = ElementActions.getText(cartBadge);
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    // ---------- Sorting ----------

    /**
     * Robust approach:
     *  - Wait for inventory items (guarantees Inventory page is rendered)
     *  - Then locate the dropdown by its class and wrap it in a Select
     */
    private Select getSortSelect() {
        // Ensure we are really on Inventory page and items are rendered
        WaitHelpers.waitForVisibility(inventoryItemContainer);

        WebElement dropdownElement = driver.findElement(sortDropdown);
        return new Select(dropdownElement);
    }

    public void sortByNameAToZ() {
        getSortSelect().selectByValue("az");
    }

    public void sortByNameZToA() {
        getSortSelect().selectByValue("za");
    }

    public void sortByPriceLowToHigh() {
        getSortSelect().selectByValue("lohi");
    }

    public void sortByPriceHighToLow() {
        getSortSelect().selectByValue("hilo");
    }

    // ---------- Reading list of items ----------

    public List<String> getAllItemNames() {
        // Make sure list is present before reading
        WaitHelpers.waitForVisibility(inventoryItemContainer);

        List<WebElement> itemElements = driver.findElements(itemNameLocator);
        List<String> names = new ArrayList<>();
        for (WebElement el : itemElements) {
            names.add(ElementActions.getText(el).trim());
        }
        return names;
    }

    public List<Double> getAllItemPrices() {
        // Make sure list is present before reading
        WaitHelpers.waitForVisibility(inventoryItemContainer);

        List<WebElement> priceElements = driver.findElements(itemPriceLocator);
        List<Double> prices = new ArrayList<>();
        for (WebElement el : priceElements) {
            String txt = ElementActions.getText(el).replace("$", "").trim();
            try {
                prices.add(Double.parseDouble(txt));
            } catch (NumberFormatException e) {
            }
        }
        return prices;
    }

    // ---------- Social icons ----------

    public void clickTwitterIcon() {
        ElementActions.click(twitterIcon);
    }

    public void clickFacebookIcon() {
        ElementActions.click(facebookIcon);
    }

    public void clickLinkedInIcon() {
        ElementActions.click(linkedinIcon);
    }

    public Double getItemPriceByNameOnInventory(String itemName) {
        WaitHelpers.waitForVisibility(inventoryItemContainer);
        List<WebElement> items = driver.findElements(inventoryItemContainer);

        for (WebElement item : items) {

            String actualName = ElementActions.getText(item,itemNameLocator).trim();
            if (actualName.equalsIgnoreCase(itemName)) {
                String priceText = ElementActions.getText(item,itemPriceLocator)
                        .replace("$", "").trim();
                try {
                    return Double.parseDouble(priceText);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public boolean isOnInventoryPage() {
        String currentUrl = BrowserUtils.getCurrentUrl();
        return currentUrl != null && currentUrl.contains("inventory.html");
    }

    public String openTwitterAndGetTargetUrl() {
        WebDriver driver = DriverManager.getDriver();
        String originalHandle = driver.getWindowHandle();

        clickTwitterIcon();

        switchToNewWindow(driver, originalHandle);
        String actualUrl = BrowserUtils.getCurrentUrl();

        driver.close();
        driver.switchTo().window(originalHandle);

        return actualUrl;
    }

    public String getExpectedTwitterUrlPrefix() {
        String expected = PropertiesUtils.getProperty("twitter.url");
        if (expected == null || expected.isBlank()) {
            throw new IllegalStateException("twitter.url property must be set in testConfig.properties");
        }
        return expected;
    }

    // ---------- Social links helpers (Facebook) ----------

    public String openFacebookAndGetTargetUrl() {
        WebDriver driver = DriverManager.getDriver();
        String originalHandle = driver.getWindowHandle();

        clickFacebookIcon();

        switchToNewWindow(driver, originalHandle);
        String actualUrl = BrowserUtils.getCurrentUrl();

        driver.close();
        driver.switchTo().window(originalHandle);

        return actualUrl;
    }

    public String getExpectedFacebookUrlPrefix() {
        String expected = PropertiesUtils.getProperty("facebook.url");
        if (expected == null || expected.isBlank()) {
            throw new IllegalStateException("facebook.url property must be set in testConfig.properties");
        }
        return expected;
    }

    // ---------- Social links helpers (LinkedIn) ----------

    public String openLinkedInAndGetTargetUrl() {
        WebDriver driver = DriverManager.getDriver();

        String originalHandle = driver.getWindowHandle();

        clickLinkedInIcon();

        switchToNewWindow(driver, originalHandle);
        String actualUrl = BrowserUtils.getCurrentUrl();

        driver.close();
        driver.switchTo().window(originalHandle);

        return actualUrl;
    }

    public String getExpectedLinkedInUrlPrefix() {
        String expected = PropertiesUtils.getProperty("linkedin.url");
        if (expected == null || expected.isBlank()) {
            throw new IllegalStateException("linkedin.url property must be set in testConfig.properties");
        }
        return expected;
    }

    // ---------- Shared window-switch helper ----------

    private void switchToNewWindow(WebDriver driver, String originalHandle) {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                return;
            }
        }
        throw new IllegalStateException("No new window/tab appeared after clicking social icon.");
    }


}
