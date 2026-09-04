package com.saucedemo.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class ProductDetailsScreen extends BaseScreen {

    private static final By PRODUCT_SCREEN = AppiumBy.accessibilityId("product screen");
    private static final By ADD_TO_CART = AppiumBy.accessibilityId("Add To Cart button");
    private static final By CONTAINER_HEADER = AppiumBy.accessibilityId("container header");

    public Header header() {
        return new Header();
    }

    public boolean isLoaded() {
        return isDisplayed(PRODUCT_SCREEN);
    }

    public String productName() {
        return textOf(CONTAINER_HEADER);
    }

    public ProductDetailsScreen addToCart() {
        step("Adding the product to the cart");
        scrollTo(ADD_TO_CART);
        tap(ADD_TO_CART);
        return this;
    }
}
