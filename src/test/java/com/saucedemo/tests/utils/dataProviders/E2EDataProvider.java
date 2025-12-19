package com.saucedemo.tests.utils.dataProviders;

import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.tests.utils.models.OrderModel;
import com.saucedemo.tests.utils.models.UserModel;
import com.saucedemo.utilities.config.JsonUtils;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class E2EDataProvider {

    private static final String E2E_ORDERS_JSON_PATH     = "/testData/E2E_orders.json";
    private static final String LOGIN_VALID_JSON_PATH    = "/testData/login_valid.json";
    private static final String INVENTORY_JSON_PATH      = "/testData/inventory_test_items.json";
    private static final String CHECKOUT_VALID_JSON_PATH = "/testData/checkout_valid.json";

    // ---------- Internal helpers (orders) ----------

    private static List<OrderModel> getAllOrders() {
        return JsonUtils.readJsonList(E2E_ORDERS_JSON_PATH, OrderModel.class);
    }

    private static OrderModel getOrderByScenarioId(String scenarioId) {
        return getAllOrders().stream()
                .filter(o -> scenarioId.equalsIgnoreCase(o.getScenarioId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No E2E order found with scenarioId = " + scenarioId +
                                " in " + E2E_ORDERS_JSON_PATH));
    }

    private static Object[][] toSingleColumnOrders(List<OrderModel> orders) {
        Object[][] data = new Object[orders.size()][1];
        for (int i = 0; i < orders.size(); i++) {
            data[i][0] = orders.get(i);
        }
        return data;
    }

    private static Object[][] toSingleRowOrder(OrderModel order) {
        return new Object[][]{{order}};
    }

    // ---------- Internal helpers (users + items + checkout) ----------

    private static List<UserModel> getAllUsers() {
        return JsonUtils.readJsonList(LOGIN_VALID_JSON_PATH, UserModel.class);
    }

    private static UserModel getStandardUser() {
        return getAllUsers().stream()
                .filter(u -> "standard".equalsIgnoreCase(u.getUserType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No standard user found in " + LOGIN_VALID_JSON_PATH));
    }

    private static List<UserModel> getPersonaUsers() {
        Set<String> personaScenarioIds = Set.of(
                "problem_user",
                "performance_glitch_user",
                "error_user",
                "visual_user"
        );

        List<UserModel> allUsers = getAllUsers();
        List<UserModel> personaUsers = allUsers.stream()
                .filter(u -> personaScenarioIds.contains(u.getScenarioId()))
                .toList();

        if (personaUsers.isEmpty()) {
            throw new IllegalStateException(
                    "No persona users (problem/performance_glitch/error/visual) found in " + LOGIN_VALID_JSON_PATH);
        }

        return personaUsers;
    }

    private static List<ItemModel> getAllInventoryItems() {
        return JsonUtils.readJsonList(INVENTORY_JSON_PATH, ItemModel.class);
    }

    private static ItemModel getDefaultInventoryItem() {
        List<ItemModel> items = getAllInventoryItems();

        if (items.isEmpty()) {
            throw new IllegalStateException("No inventory items found in " + INVENTORY_JSON_PATH);
        }
        // For generic E2E tests, any valid item is fine -> use the first one
        return items.get(0);
    }

    private static List<ItemModel> getItemsForOrder(OrderModel order) {
        List<ItemModel> allItems = getAllInventoryItems();
        List<ItemModel> result   = new ArrayList<>();

        for (String itemId : order.getItemIds()) {
            ItemModel match = allItems.stream()
                    .filter(i -> itemId.equalsIgnoreCase(i.getItemId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "No inventory item found with itemId = " + itemId +
                                    " in " + INVENTORY_JSON_PATH));
            result.add(match);
        }

        return result;
    }

    private static CheckoutModel getDefaultCheckoutData() {
        List<CheckoutModel> list =
                JsonUtils.readJsonList(CHECKOUT_VALID_JSON_PATH, CheckoutModel.class);

        if (list.isEmpty()) {
            throw new IllegalStateException("No valid checkout data found in " + CHECKOUT_VALID_JSON_PATH);
        }

        // For E2E flows, any valid customer data is fine
        return list.get(0);
    }

    // ---------- DataProviders (orders only) ----------

    @DataProvider(name = "allE2EOrders")
    public static Object[][] allE2EOrders() {
        List<OrderModel> orders = getAllOrders();
        if (orders.isEmpty()) {
            throw new IllegalStateException("No E2E orders found in " + E2E_ORDERS_JSON_PATH);
        }
        return toSingleColumnOrders(orders);
    }

    @DataProvider(name = "standardSingleItemOrder")
    public static Object[][] standardSingleItemOrder() {
        OrderModel order = getOrderByScenarioId("standard_single_item");
        return toSingleRowOrder(order);
    }

    @DataProvider(name = "standardTwoItemsOrder")
    public static Object[][] standardTwoItemsOrder() {
        OrderModel order = getOrderByScenarioId("standard_two_items");
        return toSingleRowOrder(order);
    }

    // ---------- DataProviders (orders + user) ----------

    /**
     * Provides (OrderModel, standard user) for reset-app-state and similar flows.
     */
    @DataProvider(name = "standardTwoItemsOrderWithUser")
    public static Object[][] standardTwoItemsOrderWithUser() {
        OrderModel order       = getOrderByScenarioId("standard_two_items");
        UserModel standardUser = getStandardUser();

        return new Object[][]{
                {order, standardUser}
        };
    }

    // ---------- DataProviders (user + item) ----------

    /**
     * Provides (standard user, single inventory item) pair
     * for E2E flows like logout / re-login while preserving cart.
     */
    @DataProvider(name = "standardUserWithItem")
    public static Object[][] standardUserWithItem() {
        UserModel standardUser = getStandardUser();
        ItemModel defaultItem  = getDefaultInventoryItem();

        return new Object[][]{
                {standardUser, defaultItem}
        };
    }

    /**
     * Provides (persona user, single inventory item, checkout data)
     * for persona-based E2E checkout flows.
     */
    @DataProvider(name = "personaUsersWithItemAndCheckout")
    public static Object[][] personaUsersWithItemAndCheckout() {
        List<UserModel> personaUsers = getPersonaUsers();
        ItemModel defaultItem        = getDefaultInventoryItem();
        CheckoutModel checkoutData   = getDefaultCheckoutData();

        Object[][] data = new Object[personaUsers.size()][3];
        for (int i = 0; i < personaUsers.size(); i++) {
            data[i][0] = personaUsers.get(i); // UserModel
            data[i][1] = defaultItem;         // ItemModel
            data[i][2] = checkoutData;        // CheckoutModel
        }
        return data;
    }

    // ---------- DataProviders (full standard order + user + checkout + items) ----------

    /**
     * Provides (order, standard user, checkout data, items for that order)
     * for full standard purchase flow.
     */
    @DataProvider(name = "standardSingleItemOrderFull")
    public static Object[][] standardSingleItemOrderFull() {
        OrderModel order       = getOrderByScenarioId("standard_single_item");
        UserModel standardUser = getStandardUser();
        CheckoutModel checkout = getDefaultCheckoutData();
        List<ItemModel> orderItems = getItemsForOrder(order);

        return new Object[][]{
                {order, standardUser, checkout, orderItems}
        };
    }
}
