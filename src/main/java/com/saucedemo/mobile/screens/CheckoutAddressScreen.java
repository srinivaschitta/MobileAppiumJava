package com.saucedemo.mobile.screens;

import com.saucedemo.mobile.data.DemoCheckoutForm;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class CheckoutAddressScreen extends BaseScreen {

    private static final By ADDRESS_SCREEN = AppiumBy.accessibilityId("checkout address screen");
    private static final By FULL_NAME = AppiumBy.accessibilityId("Full Name* input field");
    private static final By ADDRESS_LINE_ONE = AppiumBy.accessibilityId("Address Line 1* input field");
    private static final By CITY = AppiumBy.accessibilityId("City* input field");
    private static final By STATE_REGION = AppiumBy.accessibilityId("State/Region input field");
    private static final By ZIP_CODE = AppiumBy.accessibilityId("Zip Code* input field");
    private static final By COUNTRY = AppiumBy.accessibilityId("Country* input field");
    private static final By TO_PAYMENT = AppiumBy.accessibilityId("To Payment button");

    public boolean isLoaded() {
        return isDisplayed(ADDRESS_SCREEN) || isDisplayed(FULL_NAME);
    }

    public CheckoutPaymentScreen submitAddress(DemoCheckoutForm checkoutForm) {
        step("Submitting checkout address");
        type(FULL_NAME, checkoutForm.fullName());
        type(ADDRESS_LINE_ONE, checkoutForm.addressLineOne());
        scrollTo(CITY);
        type(CITY, checkoutForm.city());
        type(STATE_REGION, checkoutForm.stateRegion());
        scrollTo(ZIP_CODE);
        type(ZIP_CODE, checkoutForm.zipCode());
        type(COUNTRY, checkoutForm.country());
        scrollTo(TO_PAYMENT);
        tap(TO_PAYMENT);
        return new CheckoutPaymentScreen();
    }
}
