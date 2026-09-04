package com.saucedemo.mobile.screens;

import com.saucedemo.mobile.data.DemoCheckoutForm;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.util.Map;

public final class CheckoutPaymentScreen extends BaseScreen {

    private static final By PAYMENT_SCREEN = AppiumBy.accessibilityId("checkout payment screen");
    private static final By FULL_NAME = AppiumBy.accessibilityId("Full Name* input field");
    private static final By CARD_NUMBER = AppiumBy.accessibilityId("Card Number* input field");
    private static final By EXPIRATION_DATE = AppiumBy.accessibilityId("Expiration Date* input field");
    private static final By SECURITY_CODE = AppiumBy.accessibilityId("Security Code* input field");
    private static final By REVIEW_ORDER = AppiumBy.accessibilityId("Review Order button");

    public boolean isLoaded() {
        return isDisplayed(PAYMENT_SCREEN) || isDisplayed(CARD_NUMBER);
    }

    public CheckoutReviewScreen submitPayment(DemoCheckoutForm checkoutForm) {
        step("Submitting payment details");
        type(FULL_NAME, checkoutForm.cardFullName());
        type(CARD_NUMBER, checkoutForm.cardNumber());
        scrollTo(EXPIRATION_DATE);
        type(EXPIRATION_DATE, checkoutForm.expirationDate());
        type(SECURITY_CODE, checkoutForm.securityCode());
        hideKeyboardIfPresent();
        dismissKeyboardByTappingHeader();
        scrollTo(REVIEW_ORDER);
        clickReviewOrder();
        CheckoutReviewScreen reviewScreen = new CheckoutReviewScreen();
        if (!reviewScreen.isLoaded()) {
            clickReviewOrder();
        }
        reviewScreen.waitUntilLoaded();
        return reviewScreen;
    }

    private void dismissKeyboardByTappingHeader() {
        Dimension size = driver.manage().window().getSize();
        driver.executeScript("mobile: clickGesture", Map.of(
                "x", size.getWidth() / 2,
                "y", size.getHeight() / 10));
    }

    private void clickReviewOrder() {
        log.info("Tapping {}", REVIEW_ORDER);
        WebElement reviewOrder = waitUntilVisible(REVIEW_ORDER);
        Rectangle bounds = reviewOrder.getRect();
        driver.executeScript("mobile: clickGesture", Map.of(
                "x", bounds.getX() + bounds.getWidth() / 2,
                "y", bounds.getY() + bounds.getHeight() / 2));
    }
}
