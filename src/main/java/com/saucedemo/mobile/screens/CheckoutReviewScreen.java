package com.saucedemo.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class CheckoutReviewScreen extends BaseScreen {

    private static final By REVIEW_SCREEN = AppiumBy.accessibilityId("checkout review order screen");
    private static final By PLACE_ORDER = AppiumBy.accessibilityId("Place Order button");

    public boolean isLoaded() {
        return isDisplayed(REVIEW_SCREEN) || isDisplayed(PLACE_ORDER);
    }

    public void waitUntilLoaded() {
        waitUntilVisible(REVIEW_SCREEN);
    }

    public CheckoutCompleteScreen placeOrder() {
        step("Placing the order");
        waitUntilLoaded();
        scrollTo(PLACE_ORDER);
        tap(PLACE_ORDER);
        return new CheckoutCompleteScreen();
    }
}
