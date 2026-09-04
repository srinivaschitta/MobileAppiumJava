package com.saucedemo.mobile.screens;

import com.saucedemo.mobile.data.DemoUser;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class LoginScreen extends BaseScreen {

    private static final By USERNAME_INPUT = AppiumBy.accessibilityId("Username input field");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("Password input field");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("Login button");
    private static final By GENERIC_ERROR = AppiumBy.accessibilityId("generic-error-message");
    private static final By USERNAME_ERROR = AppiumBy.accessibilityId("Username-error-message");
    private static final By PASSWORD_ERROR = AppiumBy.accessibilityId("Password-error-message");

    public boolean isLoaded() {
        return isDisplayed(LOGIN_BUTTON);
    }

    public void waitUntilLoaded() {
        waitUntilVisible(LOGIN_BUTTON);
    }

    public LoginScreen enterUsername(String username) {
        step("Entering username");
        type(USERNAME_INPUT, username);
        return this;
    }

    public LoginScreen enterPassword(String password) {
        step("Entering password");
        type(PASSWORD_INPUT, password);
        return this;
    }

    public void tapLogin() {
        step("Tapping login");
        tap(LOGIN_BUTTON);
    }

    public LoginScreen submitCredentials(DemoUser user) {
        enterUsername(user.username());
        enterPassword(user.password());
        tapLogin();
        return this;
    }

    public CatalogScreen loginAs(DemoUser user) {
        submitCredentials(user);
        return new CatalogScreen();
    }

    public boolean isErrorVisible() {
        return isDisplayed(GENERIC_ERROR) || isDisplayed(USERNAME_ERROR) || isDisplayed(PASSWORD_ERROR);
    }

    public String errorText() {
        if (isDisplayed(GENERIC_ERROR, java.time.Duration.ofSeconds(2))) {
            return textOf(GENERIC_ERROR);
        }
        if (isDisplayed(PASSWORD_ERROR, java.time.Duration.ofSeconds(1))) {
            return textOf(PASSWORD_ERROR);
        }
        return textOf(USERNAME_ERROR);
    }
}
