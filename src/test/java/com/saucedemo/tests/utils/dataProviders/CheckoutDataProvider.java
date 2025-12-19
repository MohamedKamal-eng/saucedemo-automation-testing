package com.saucedemo.tests.utils.dataProviders;

import com.saucedemo.tests.utils.models.CheckoutModel;
import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.utilities.config.JsonUtils;
import org.testng.annotations.DataProvider;

import java.util.List;

public class CheckoutDataProvider {

    private static final String CHECKOUT_VALID_JSON_PATH = "/testData/checkout_valid.json";
    private static final String CHECKOUT_INVALID_JSON_PATH = "/testData/checkout_invalid.json";
    private static final String INVENTORY_JSON_PATH = "/testData/inventory_test_items.json";

    // ---------- Internal helpers ----------

    private static List<CheckoutModel> getValidData() {
        return JsonUtils.readJsonList(CHECKOUT_VALID_JSON_PATH, CheckoutModel.class);
    }

    private static List<CheckoutModel> getInvalidData() {
        return JsonUtils.readJsonList(CHECKOUT_INVALID_JSON_PATH, CheckoutModel.class);
    }

    private static CheckoutModel getSingleInvalidScenario(String scenarioId) {
        return getInvalidData().stream()
                .filter(c -> scenarioId.equalsIgnoreCase(c.getScenarioId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No invalid checkout scenario found with scenarioId = " + scenarioId +
                                " in " + CHECKOUT_INVALID_JSON_PATH));
    }

    private static ItemModel getDefaultInventoryItem() {
        List<ItemModel> items =
                JsonUtils.readJsonList(INVENTORY_JSON_PATH, ItemModel.class);

        if (items.isEmpty()) {
            throw new IllegalStateException("No inventory items found in " + INVENTORY_JSON_PATH);
        }
        // For these tests, any valid item is fine -> just use the first one
        return items.get(0);
    }

    private static Object[][] toSingleColumn(List<CheckoutModel> list) {
        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i);
        }
        return data;
    }

    private static Object[][] toSingleRow(CheckoutModel model) {
        return new Object[][]{{model}};
    }

    // ---------- DataProviders ----------

    /**
     * Only CheckoutModel – for tests that don't need cart precondition / item.
     */
    @DataProvider(name = "validCheckoutData")
    public static Object[][] validCheckoutData() {
        List<CheckoutModel> list = getValidData();
        if (list.isEmpty()) {
            throw new IllegalStateException("No valid checkout data found in " + CHECKOUT_VALID_JSON_PATH);
        }
        return toSingleColumn(list);   // 1 column: CheckoutModel
    }

    /**
     * (CheckoutModel, ItemModel) – for tests that need an item in cart before checkout.
     */
    @DataProvider(name = "validCheckoutDataWithItem")
    public static Object[][] validCheckoutDataWithItem() {
        List<CheckoutModel> checkoutList = getValidData();
        if (checkoutList.isEmpty()) {
            throw new IllegalStateException("No valid checkout data found in " + CHECKOUT_VALID_JSON_PATH);
        }

        ItemModel defaultItem = getDefaultInventoryItem();

        Object[][] data = new Object[checkoutList.size()][2];
        for (int i = 0; i < checkoutList.size(); i++) {
            data[i][0] = checkoutList.get(i);   // CheckoutModel
            data[i][1] = defaultItem;           // ItemModel
        }
        return data;
    }

    // ---------- Invalid / negative scenarios (single-column) ----------

    @DataProvider(name = "missingFirstName")
    public static Object[][] missingFirstName() {
        CheckoutModel scenario = getSingleInvalidScenario("missing_first_name");
        return toSingleRow(scenario);
    }

    @DataProvider(name = "missingLastName")
    public static Object[][] missingLastName() {
        CheckoutModel scenario = getSingleInvalidScenario("missing_last_name");
        return toSingleRow(scenario);
    }

    @DataProvider(name = "missingPostalCode")
    public static Object[][] missingPostalCode() {
        CheckoutModel scenario = getSingleInvalidScenario("missing_postal_code");
        return toSingleRow(scenario);
    }

    // ---------- Invalid / negative scenarios WITH item (for Step One tests) ----------

    @DataProvider(name = "missingFirstNameWithItem")
    public static Object[][] missingFirstNameWithItem() {
        CheckoutModel scenario = getSingleInvalidScenario("missing_first_name");
        ItemModel defaultItem = getDefaultInventoryItem();

        return new Object[][]{
                {scenario, defaultItem}
        };
    }

    @DataProvider(name = "missingLastNameWithItem")
    public static Object[][] missingLastNameWithItem() {
        CheckoutModel scenario = getSingleInvalidScenario("missing_last_name");
        ItemModel defaultItem = getDefaultInventoryItem();

        return new Object[][]{
                {scenario, defaultItem}
        };
    }

    @DataProvider(name = "missingPostalCodeWithItem")
    public static Object[][] missingPostalCodeWithItem() {
        CheckoutModel scenario = getSingleInvalidScenario("missing_postal_code");
        ItemModel defaultItem = getDefaultInventoryItem();

        return new Object[][]{
                {scenario, defaultItem}
        };
    }
}
