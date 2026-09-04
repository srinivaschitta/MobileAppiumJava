package com.saucedemo.mobile.tests;

import com.saucedemo.mobile.driver.DriverFactory;
import com.saucedemo.mobile.driver.DriverManager;
import com.saucedemo.mobile.screens.AppStateReset;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void startSession() {
        DriverFactory.createDriver();
        AppStateReset.resetIfConfigured();
    }

    @AfterMethod(alwaysRun = true)
    public void stopSession() {
        AppiumDriver driver = DriverManager.getOrNull();
        if (driver != null) {
            driver.quit();
        }
        DriverManager.unload();
    }
}
