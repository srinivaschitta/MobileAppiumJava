package com.saucedemo.mobile.assertion;

import com.saucedemo.mobile.screens.CheckoutCompleteScreen;

import static org.assertj.core.api.Assertions.assertThat;

public final class CheckoutAssert {

    private CheckoutAssert() {
    }

    public static void assertOrderIsComplete(CheckoutCompleteScreen checkoutCompleteScreen) {
        assertThat(checkoutCompleteScreen.isLoaded()).isTrue();
    }
}
