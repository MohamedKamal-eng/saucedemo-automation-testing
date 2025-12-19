package com.saucedemo.tests.utils.dataProviders;

import com.saucedemo.tests.utils.models.UserModel;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.stream.Collectors;

import static com.saucedemo.tests.utils.dataProviders.TestDataUtils.*;


public class LoginDataProvider {


    private static UserModel getSingleInvalidScenario(String scenarioId) {
        return TestDataUtils.getInvalidUsers().stream()
                .filter(u -> scenarioId.equalsIgnoreCase(u.getScenarioId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No invalid login scenario found with scenarioId = " + scenarioId +
                                " in " + LOGIN_INVALID_JSON_PATH));
    }

    private static Object[][] toSingleColumnData(List<UserModel> users) {
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }
        return data;
    }

    private static Object[][] toSingleRow(UserModel user) {
        return new Object[][]{{user}};
    }

    @DataProvider(name = "standardUser")
    public static Object[][] standardUser() {
        List<UserModel> valid = TestDataUtils.getValidUsers().stream()
                .filter(u -> "standard".equalsIgnoreCase(u.getUserType()))
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            throw new IllegalStateException("No user with userType = 'standard' found in " + LOGIN_VALID_JSON_PATH);
        }
        return toSingleColumnData(valid);
    }

    @DataProvider(name = "lockedOutUser")
    public static Object[][] lockedOutUser() {
        UserModel lockedOut = getSingleInvalidScenario("locked_out_user");
        return toSingleRow(lockedOut);
    }

    @DataProvider(name = "emptyUsername")
    public static Object[][] emptyUsername() {
        UserModel scenario = getSingleInvalidScenario("empty_username");
        return toSingleRow(scenario);
    }

    @DataProvider(name = "emptyPassword")
    public static Object[][] emptyPassword() {
        UserModel scenario = getSingleInvalidScenario("empty_password");
        return toSingleRow(scenario);
    }

    @DataProvider(name = "emptyBoth")
    public static Object[][] emptyBoth() {
        UserModel scenario = getSingleInvalidScenario("empty_both");
        return toSingleRow(scenario);
    }

    @DataProvider(name = "invalidUsername")
    public static Object[][] invalidUsername() {
        UserModel scenario = getSingleInvalidScenario("invalid_username");
        return toSingleRow(scenario);
    }

    @DataProvider(name = "invalidPassword")
    public static Object[][] invalidPassword() {
        UserModel scenario = getSingleInvalidScenario("invalid_password");
        return toSingleRow(scenario);
    }

    @DataProvider(name = "invalidBoth")
    public static Object[][] invalidBoth() {
        UserModel scenario = getSingleInvalidScenario("invalid_both");
        return toSingleRow(scenario);
    }

    /**
     * Persona users: problem_user, performance_glitch_user, error_user, visual_user.
     */
    @DataProvider(name = "personaUsers")
    public static Object[][] personaUsers() {
        List<UserModel> personas = TestDataUtils.getValidUsers().stream()
                .filter(u -> !"standard".equalsIgnoreCase(u.getUserType()))
                .collect(Collectors.toList());

        if (personas.isEmpty()) {
            throw new IllegalStateException(
                    "No persona users (non-standard) found in " + LOGIN_VALID_JSON_PATH);
        }
        return toSingleColumnData(personas);
    }
}
