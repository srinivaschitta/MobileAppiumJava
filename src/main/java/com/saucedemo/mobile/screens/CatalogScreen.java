package com.saucedemo.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class CatalogScreen extends BaseScreen {

    private static final By PRODUCTS_SCREEN = AppiumBy.accessibilityId("products screen");
    private static final By STORE_ITEM = AppiumBy.accessibilityId("store item");

    public Header header() {
        return new Header();
    }

    public LoginScreen openLogin() {
        header().openMenu();
        return new MenuDrawer().openLogin();
    }

    public void waitUntilLoaded() {
        waitUntilVisible(PRODUCTS_SCREEN);
    }

    public boolean isLoaded() {
        return isDisplayed(PRODUCTS_SCREEN);
    }

    public ProductDetailsScreen openProduct(String productName) {
        step("Opening catalog item " + productName);
        waitUntilLoaded();
        By product = AppiumBy.androidUIAutomator(
                "new UiSelector().description(\"store item\").childSelector(new UiSelector().textContains(\""
                        + productName + "\"))");
        if (!isDisplayed(product, java.time.Duration.ofSeconds(3))) {
            product = By.xpath(
                    "//android.widget.TextView[contains(@text,'" + productName
                            + "')]//ancestor::*[@content-desc='store item']");
        }
        scrollTo(product);
        tap(product);
        return new ProductDetailsScreen();
    }

    public boolean hasStoreItems() {
        return isDisplayed(STORE_ITEM);
    }
}
