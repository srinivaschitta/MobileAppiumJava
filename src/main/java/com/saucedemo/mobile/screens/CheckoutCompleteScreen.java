package com.saucedemo.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class CheckoutCompleteScreen extends BaseScreen {

    private static final By COMPLETE_SCREEN = AppiumBy.accessibilityId("checkout complete screen");
    private static final By CONTINUE_SHOPPING = AppiumBy.accessibilityId("Continue Shopping button");

    public boolean isLoaded() {
        return isDisplayed(COMPLETE_SCREEN) || isDisplayed(CONTINUE_SHOPPING);
    }
}
