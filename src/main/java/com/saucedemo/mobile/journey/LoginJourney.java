package com.saucedemo.mobile.journey;

import com.saucedemo.mobile.data.DemoUser;
import com.saucedemo.mobile.screens.CatalogScreen;
import com.saucedemo.mobile.screens.LoginScreen;

public final class LoginJourney {

    public CatalogScreen loginWith(DemoUser user) {
        return new CatalogScreen().openLogin().loginAs(user);
    }

    public LoginScreen submitInvalidCredentials(DemoUser user) {
        return new CatalogScreen().openLogin().submitCredentials(user);
    }
}
