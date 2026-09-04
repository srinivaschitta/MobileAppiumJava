package com.saucedemo.mobile.driver;

import io.appium.java_client.AppiumDriver;

public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void set(AppiumDriver driver) {
        DRIVER.set(driver);
    }

    public static AppiumDriver get() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No Appium driver is bound to this thread");
        }
        return driver;
    }

    public static AppiumDriver getOrNull() {
        return DRIVER.get();
    }

    public static void unload() {
        DRIVER.remove();
    }
}
