package com.saucedemo.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class CartScreen extends BaseScreen {

    private static final By CART_SCREEN = AppiumBy.accessibilityId("cart screen");
    private static final By PRODUCT_ROW = AppiumBy.accessibilityId("product row");
    private static final By PROCEED_TO_CHECKOUT = AppiumBy.accessibilityId("Proceed To Checkout button");
    private static final By CHECKOUT_FOOTER = AppiumBy.accessibilityId("checkout footer");

    public boolean isLoaded() {
        return isDisplayed(CART_SCREEN);
    }

    public boolean hasProduct(String productName) {
        By row = By.xpath(
                "//android.widget.TextView[contains(@text,'" + productName
                        + "')]//ancestor::*[@content-desc='product row']");
        return isDisplayed(row);
    }

    public boolean hasLineItems() {
        return isDisplayed(PRODUCT_ROW);
    }

    public String footerText() {
        return textOf(CHECKOUT_FOOTER);
    }

    public CheckoutAddressScreen proceedToCheckout() {
        step("Proceeding to checkout");
        tap(PROCEED_TO_CHECKOUT);
        return new CheckoutAddressScreen();
    }
}
