package com.saucedemo.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class Header extends BaseScreen {

    private static final By OPEN_MENU = AppiumBy.accessibilityId("open menu");
    private static final By CART_BADGE = AppiumBy.accessibilityId("cart badge");
    private static final By CONTAINER_HEADER = AppiumBy.accessibilityId("container header");

    public void openMenu() {
        step("Opening the navigation menu");
        tap(OPEN_MENU);
    }

    public CartScreen openCart() {
        step("Opening the cart from the header badge");
        tap(CART_BADGE);
        return new CartScreen();
    }

    public String headerText() {
        return textOf(CONTAINER_HEADER);
    }

    public boolean isHeaderVisible() {
        return isDisplayed(CONTAINER_HEADER);
    }

    public String cartBadgeText() {
        return textOf(CART_BADGE);
    }
}
