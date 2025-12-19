package com.saucedemo.pageComponents;

import com.saucedemo.pages.BasePage;
import com.saucedemo.utilities.selenium.helperClasses.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;


public class CartItemComponent extends BasePage {

    private static final By CART_ITEM_CONTAINER = By.cssSelector(".cart_item");
    private static final By ITEM_NAME = By.cssSelector(".inventory_item_name");



    public static boolean isItemPresentByName(String itemName) {
        List<WebElement> items = driver.findElements(CART_ITEM_CONTAINER);
        for (WebElement item : items) {
            String name = ElementActions.getText(item,ITEM_NAME).trim();
            if (name.equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
}
