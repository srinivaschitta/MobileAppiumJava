package com.saucedemo.mobile.assertion;

import com.saucedemo.mobile.screens.CatalogScreen;
import com.saucedemo.mobile.screens.LoginScreen;

import static org.assertj.core.api.Assertions.assertThat;

public final class LoginAssert {

    private LoginAssert() {
    }

    public static void assertCatalogIsShownAfterLogin(CatalogScreen catalogScreen) {
        assertThat(catalogScreen.isLoaded()).isTrue();
        assertThat(new LoginScreen().isLoaded()).isFalse();
    }

    public static void assertErrorStaysOnLoginScreen(LoginScreen loginScreen) {
        assertThat(loginScreen.isLoaded()).isTrue();
        assertThat(loginScreen.isErrorVisible()).isTrue();
        assertThat(loginScreen.errorText()).isNotBlank();
    }

    public static void assertLockedOutMessageIsShown(LoginScreen loginScreen) {
        assertErrorStaysOnLoginScreen(loginScreen);
        assertThat(loginScreen.errorText()).containsIgnoringCase("locked");
    }
}
