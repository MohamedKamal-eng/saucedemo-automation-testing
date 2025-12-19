package com.saucedemo.tests.utils.dataProviders;

import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.tests.utils.models.UserModel;
import com.saucedemo.utilities.config.JsonUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TestDataUtils {
    public static final String LOGIN_VALID_JSON_PATH = "/testData/login_valid.json";
    public static final String LOGIN_INVALID_JSON_PATH = "/testData/login_invalid.json";
    public static final String INVENTORY_TEST_ITEMS_JSON_PATH = "/testData/inventory_test_items.json";
    public static final String CHECKOUT_Valid_JSON_PATH = "/testData/checkout_valid.json";

    public static UserModel getStandardUser() {
        List<UserModel> users =
                JsonUtils.readJsonList(LOGIN_VALID_JSON_PATH, UserModel.class);
        return users.stream()
                .filter(u -> "standard".equalsIgnoreCase(u.getUserType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No standard user found in login_valid.json"));
    }

    public static List<UserModel> getValidUsers() {
    return JsonUtils.readJsonList(LOGIN_VALID_JSON_PATH, UserModel.class);
    }
    public static List<UserModel> getInvalidUsers() {
        return JsonUtils.readJsonList(LOGIN_INVALID_JSON_PATH, UserModel.class);
    }

    public static ItemModel getItemById(String itemId) {
        List<ItemModel> items =
                JsonUtils.readJsonList(INVENTORY_TEST_ITEMS_JSON_PATH, ItemModel.class);
        return items.stream()
                .filter(i -> itemId.equalsIgnoreCase(i.getItemId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No item found with itemId = " + itemId + INVENTORY_TEST_ITEMS_JSON_PATH));
    }

    public static ItemModel getAnyInventoryItem() {
        List<ItemModel> items =
                JsonUtils.readJsonList(INVENTORY_TEST_ITEMS_JSON_PATH, ItemModel.class);
        if (items.isEmpty()) {
            throw new IllegalStateException("No items defined in" + INVENTORY_TEST_ITEMS_JSON_PATH);
        }
        return items.get(0);
    }

    public static CheckoutModel getValidCheckoutData() {
        List<CheckoutModel> list =
                JsonUtils.readJsonList(CHECKOUT_Valid_JSON_PATH, CheckoutModel.class);
        if (list.isEmpty()) {
            throw new IllegalStateException("No valid checkout data found in checkout_valid.json");
        }
        return list.get(0); // single happy-path entry is enough
    }
//    public static OrderModel getOrderByScenario(String scenarioId) {}
    public static UserModel getErrorUser() {
        List<UserModel> users =
                JsonUtils.readJsonList(LOGIN_VALID_JSON_PATH, UserModel.class);

        return users.stream()
                .filter(u -> "error".equalsIgnoreCase(u.getUserType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No error user found in " + LOGIN_VALID_JSON_PATH +
                                ". Ensure login_valid.json contains a userType = 'error'."));
    }

    public static List<UserModel> getStandardAndErrorUsers() {
        List<UserModel> users =
                JsonUtils.readJsonList(LOGIN_VALID_JSON_PATH, UserModel.class);

        return users.stream()
                .filter(u -> "standard".equalsIgnoreCase(u.getUserType())
                        || "error".equalsIgnoreCase(u.getUserType()))
                .collect(Collectors.toList());
    }
}


