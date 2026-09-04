package com.saucedemo.mobile.tests;

import com.saucedemo.mobile.assertion.LoginAssert;
import com.saucedemo.mobile.data.DemoUser;
import com.saucedemo.mobile.journey.LoginJourney;
import com.saucedemo.mobile.retry.ControlledRetryAnalyzer;
import com.saucedemo.mobile.screens.CatalogScreen;
import com.saucedemo.mobile.screens.LoginScreen;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(
            groups = {"smoke", "login"},
            retryAnalyzer = ControlledRetryAnalyzer.class,
            description = "Given a standard user, when they log in, then the catalog is shown")
    public void validateLoginSucceedsWithValidCredentials() {
        DemoUser standardUser = DemoUser.standardUser();

        CatalogScreen catalogScreen = new LoginJourney().loginWith(standardUser);

        LoginAssert.assertCatalogIsShownAfterLogin(catalogScreen);
    }

    @Test(
            groups = {"smoke", "login"},
            description = "Given a wrong password, when they log in, then an error stays on the login screen")
    public void validateLoginRejectedWithWrongPassword() {
        DemoUser wrongPasswordUser = DemoUser.wrongPasswordUser();

        LoginScreen loginScreen = new LoginJourney().submitInvalidCredentials(wrongPasswordUser);

        LoginAssert.assertErrorStaysOnLoginScreen(loginScreen);
    }

    @Test(
            groups = {"login"},
            description = "Given a locked user, when they log in, then the locked-out error is shown")
    public void validateLoginRejectedForLockedUser() {
        DemoUser lockedOutUser = DemoUser.lockedOutUser();

        LoginScreen loginScreen = new LoginJourney().submitInvalidCredentials(lockedOutUser);

        LoginAssert.assertLockedOutMessageIsShown(loginScreen);
    }
}
