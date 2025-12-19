package com.saucedemo.tests.base;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.utils.models.UserModel;
import com.saucedemo.utilities.config.JsonUtils;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.List;


@Epic("SauceDemo Web Application")
@Feature("Authentication – Logged-in Preconditions")
@Owner("Mohamed Kamal")
public abstract class LoginBaseTest extends BaseTest {

    private static final String LOGIN_VALID_JSON_PATH = "/testData/login_valid.json";

    protected UserModel standardUser;

    // -------------------- Precondition: load standard user from JSON --------------------

    @BeforeClass(alwaysRun = true)
    @Step("Load standard user data from JSON file: {LOGIN_VALID_JSON_PATH}")
    public void loadStandardUserFromJson() {
        List<UserModel> users = JsonUtils.readJsonList(LOGIN_VALID_JSON_PATH, UserModel.class);

        standardUser = users.stream()
                .filter(u -> "standard".equalsIgnoreCase(u.getUserType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No standard user found in " + LOGIN_VALID_JSON_PATH +
                                ". Ensure login_valid.json contains a userType = 'standard'."));
    }

    // -------------------- Precondition: login before each Inventory test --------------------

    @BeforeMethod(alwaysRun = true)
    @Step("Login as standard user before test and navigate to Inventory page")
    public void loginBeforeEachInventoryTest() {
        // BaseTest.setUp() has already run here (driver + baseUrl ready)

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(standardUser.getUsername(), standardUser.getPassword());

    }
}
