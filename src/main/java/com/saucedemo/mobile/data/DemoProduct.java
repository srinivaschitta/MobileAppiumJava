package com.saucedemo.mobile.data;

public final class DemoProduct {

    private final String catalogSearchName;
    private final String cartDisplayName;

    private DemoProduct(String catalogSearchName, String cartDisplayName) {
        this.catalogSearchName = catalogSearchName;
        this.cartDisplayName = cartDisplayName;
    }

    public static DemoProduct backpack() {
        return new DemoProduct("Backpack", "Sauce Labs Backpack");
    }

    public String catalogSearchName() {
        return catalogSearchName;
    }

    public String cartDisplayName() {
        return cartDisplayName;
    }
}
