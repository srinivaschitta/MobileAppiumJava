package com.saucedemo.mobile.screens;

import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.driver.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

public final class AppStateReset {

    private static final Logger log = LoggerFactory.getLogger(AppStateReset.class);
    private static final By RESET_HEADER = AppiumBy.accessibilityId("longpress reset app");

    private AppStateReset() {
    }

    public static void resetIfConfigured() {
        if (!"longpress-header".equals(ConfigManager.getInstance().appResetMode())) {
            return;
        }
        AppiumDriver driver = DriverManager.get();
        log.info("Resetting My Demo App store via long-press on the header");
        longPressHeaderIfPresent(driver);
        relaunchToLaunchScreen(driver);
        new CatalogScreen().waitUntilLoaded();
    }

    private static void longPressHeaderIfPresent(AppiumDriver driver) {
        CatalogScreen catalogScreen = new CatalogScreen();
        if (!catalogScreen.isDisplayed(RESET_HEADER, Duration.ofSeconds(3))) {
            log.info("Header reset control is not visible; skipping long-press");
            return;
        }
        WebElement header = catalogScreen.waitUntilVisible(RESET_HEADER);
        driver.executeScript("mobile: longClickGesture", Map.of(
                "elementId", ((RemoteWebElement) header).getId(),
                "duration", 1000));
    }

    private static void relaunchToLaunchScreen(AppiumDriver driver) {
        if (!(driver instanceof AndroidDriver androidDriver)) {
            return;
        }
        String appId = ConfigManager.getInstance().appPackage();
        log.info("Relaunching {} so leftover screens do not block catalog reset", appId);
        androidDriver.terminateApp(appId);
        androidDriver.activateApp(appId);
    }
}
