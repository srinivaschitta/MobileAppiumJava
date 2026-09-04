package com.saucedemo.mobile.screens;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class MenuDrawer extends BaseScreen {

    private static final By MENU_ITEM_LOGIN = AppiumBy.accessibilityId("menu item log in");
    private static final By MENU_ITEM_CATALOG = AppiumBy.accessibilityId("menu item catalog");

    public LoginScreen openLogin() {
        step("Opening login from the menu");
        scrollTo(MENU_ITEM_LOGIN);
        tap(MENU_ITEM_LOGIN);
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.waitUntilLoaded();
        return loginScreen;
    }

    public CatalogScreen openCatalog() {
        step("Opening catalog from the menu");
        tap(MENU_ITEM_CATALOG);
        return new CatalogScreen();
    }
}
