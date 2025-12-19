package com.saucedemo.tests.login;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.base.BaseTest;
import com.saucedemo.tests.utils.dataProviders.LoginDataProvider;
import com.saucedemo.tests.utils.models.UserModel;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Web Application")
@Feature("Login – Authentication Scenarios")
@Owner("Mohamed Kamal")
public class LoginTests extends BaseTest {

    /**
     * TC_LOGIN_001 - Verify login is successful with valid standard_user credentials.
     */
    @Test(
            description = "Validate successful login for a standard user",
            dataProvider = "standardUser",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "smoke", "regression"}
    )
    @Story("Valid login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that the standard_user can successfully log in and reach the Inventory page.")
    public void TC_LOGIN_001_validLoginStandardUser(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        boolean onInventory = loginPage.waitUntilOnInventoryPage();

        Assert.assertTrue(
                onInventory,
                "User should be redirected to Inventory page after valid login with standard user."
        );
    }

    /**
     * TC_LOGIN_002 - Verify locked_out_user cannot log in and sees locked out error message.
     */
    @Test(
            description = "Validate login failure for locked_out_user",
            dataProvider = "lockedOutUser",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "negative", "regression"}
    )
    @Story("Invalid login – locked out user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that a locked_out_user cannot log in and the correct error message is displayed.")
    public void TC_LOGIN_002_lockedOutUserCannotLogin(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should be displayed for locked_out_user."
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                user.getExpectedErrorMessage(),
                "Error message for locked_out_user does not match expected."
        );
    }

    /**
     * TC_LOGIN_003 - Verify error is shown when username is empty.
     */
    @Test(
            description = "Validate error when username is empty",
            dataProvider = "emptyUsername",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "negative", "regression"}
    )
    @Story("Invalid login – empty username")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that logging in with an empty username shows the correct validation error.")
    public void TC_LOGIN_003_emptyUsernameShowsError(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should be displayed when username is empty."
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                user.getExpectedErrorMessage(),
                "Error message for empty username does not match expected."
        );
    }

    /**
     * TC_LOGIN_004 - Verify error is shown when password is empty.
     */
    @Test(
            description = "Validate error when password is empty",
            dataProvider = "emptyPassword",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "negative", "regression"}
    )
    @Story("Invalid login – empty password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that logging in with an empty password results in a relevant validation error.")
    public void TC_LOGIN_004_emptyPasswordShowsError(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should be displayed when password is empty."
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                user.getExpectedErrorMessage(),
                "Error message for empty password does not match expected."
        );
    }

    /**
     * TC_LOGIN_005 - Verify error when both username and password are empty.
     */
    @Test(
            description = "Validate error when both username and password are empty",
            dataProvider = "emptyBoth",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "negative", "regression"}
    )
    @Story("Invalid login – empty username & password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that leaving both fields empty displays the correct validation error.")
    public void TC_LOGIN_005_emptyUsernameAndPasswordShowsError(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should be displayed when both fields are empty."
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                user.getExpectedErrorMessage(),
                "Error message for empty username & password does not match expected."
        );
    }

    /**
     * TC_LOGIN_006 - Verify error for invalid username with valid password.
     */
    @Test(
            description = "Validate error for invalid username",
            dataProvider = "invalidUsername",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "negative", "regression"}
    )
    @Story("Invalid login – wrong username")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that entering an invalid username with a valid password shows the correct error message.")
    public void TC_LOGIN_006_invalidUsernameShowsError(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid username."
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                user.getExpectedErrorMessage(),
                "Error message for invalid username does not match expected."
        );
    }

    /**
     * TC_LOGIN_007 - Verify error for valid username with invalid password.
     */
    @Test(
            description = "Validate error when password is invalid",
            dataProvider = "invalidPassword",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "negative", "regression"}
    )
    @Story("Invalid login – wrong password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that logging in with a valid username but invalid password shows an error.")
    public void TC_LOGIN_007_invalidPasswordShowsError(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid password."
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                user.getExpectedErrorMessage(),
                "Error message for invalid password does not match expected."
        );
    }

    /**
     * TC_LOGIN_008 - Verify error for invalid username and invalid password together.
     */
    @Test(
            description = "Validate error for invalid username and invalid password",
            dataProvider = "invalidBoth",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "negative", "regression"}
    )
    @Story("Invalid login – wrong username & password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that invalid credentials for both fields result in the correct error message.")
    public void TC_LOGIN_008_invalidUsernameAndPasswordShowsError(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid username & password."
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                user.getExpectedErrorMessage(),
                "Error message for invalid credentials does not match expected."
        );
    }

    /**
     * TC_LOGIN_009 - Verify persona users (problem, performance_glitch, error, visual) can log in successfully.
     * This is a light smoke to ensure these accounts are at least able to log in.
     */
    @Test(
            description = "Validate persona users can successfully log in",
            dataProvider = "personaUsers",
            dataProviderClass = LoginDataProvider.class,
            groups = {"login", "smoke", "regression"}
    )
    @Story("Persona login – problem/performance/error/visual users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirms that all persona-type accounts can successfully login and reach Inventory.")
    public void TC_LOGIN_009_personaUsersCanLoginSuccessfully(UserModel user) {

        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(user.getUsername(), user.getPassword());

        boolean onInventory = loginPage.waitUntilOnInventoryPage();

        Assert.assertTrue(
                onInventory,
                "Persona user '" + user.getScenarioId() + "' should be able to login and reach Inventory page."
        );
    }
}
