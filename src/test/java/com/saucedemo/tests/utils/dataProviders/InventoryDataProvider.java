package com.saucedemo.tests.utils.dataProviders;

import com.saucedemo.tests.utils.models.ItemModel;
import com.saucedemo.utilities.config.JsonUtils;
import org.testng.annotations.DataProvider;

import java.util.List;

public class InventoryDataProvider {

    private static final String INVENTORY_ITEMS_JSON_PATH = "/testData/inventory_test_items.json";

    private static List<ItemModel> getAllItems() {
        return JsonUtils.readJsonList(INVENTORY_ITEMS_JSON_PATH, ItemModel.class);
    }

    private static Object[][] toSingleColumn(List<ItemModel> items) {
        Object[][] data = new Object[items.size()][1];
        for (int i = 0; i < items.size(); i++) {
            data[i][0] = items.get(i);
        }
        return data;
    }


    @DataProvider(name = "allInventoryItems")
    public static Object[][] allInventoryItems() {
        return toSingleColumn(getAllItems());
    }


    @DataProvider(name = "singleInventoryItem")
    public static Object[][] singleInventoryItem() {
        List<ItemModel> items = getAllItems();
        if (items.isEmpty()) {
            throw new IllegalStateException("No items defined in " + INVENTORY_ITEMS_JSON_PATH);
        }
        return new Object[][]{{items.get(0)}};
    }
}
