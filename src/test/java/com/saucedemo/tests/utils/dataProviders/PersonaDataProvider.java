package com.saucedemo.tests.utils.dataProviders;

import com.saucedemo.tests.utils.models.UserModel;
import com.saucedemo.utilities.config.JsonUtils;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.stream.Collectors;

public class PersonaDataProvider {

    private static final String LOGIN_VALID_JSON_PATH = "/testData/login_valid.json";

    private static List<UserModel> getValidUsers() {
        return JsonUtils.readJsonList(LOGIN_VALID_JSON_PATH, UserModel.class);
    }

    private static List<UserModel> getUsersByType(List<String> userTypes) {
        return getValidUsers().stream()
                .filter(u -> userTypes.stream()
                        .anyMatch(t -> t.equalsIgnoreCase(u.getUserType())))
                .collect(Collectors.toList());
    }

    @DataProvider(name = "standardAndErrorUsers")
    public static Object[][] standardAndErrorUsers() {
        List<UserModel> users = getUsersByType(List.of("standard", "error"));

        if (users.isEmpty()) {
            throw new IllegalStateException(
                    "No users found with userType standard or error in " + LOGIN_VALID_JSON_PATH
            );
        }

        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }
        return data;
    }
}
