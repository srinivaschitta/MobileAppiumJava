package com.saucedemo.mobile.provider;

import com.saucedemo.mobile.config.ConfigException;
import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.config.DeviceSpec;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Capabilities;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public final class LocalEmulatorProvider implements ExecutionProvider {

    @Override
    public String providerName() {
        return "local";
    }

    @Override
    public UiAutomator2Options buildCapabilities(DeviceSpec device) {
        ConfigManager config = ConfigManager.getInstance();
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(config.platformName())
                .setAutomationName(config.automationName())
                .setDeviceName(device.deviceName())
                .setPlatformVersion(device.platformVersion())
                .setAppPackage(config.appPackage())
                .setAppActivity(config.appActivity())
                .setAppWaitActivity(config.appWaitActivity())
                .setAutoGrantPermissions(true)
                .setNoReset(true)
                .setNewCommandTimeout(Duration.ofSeconds(120));
        Path appFile = Path.of(config.appPath());
        if (Files.exists(appFile)) {
            options.setApp(appFile.toAbsolutePath().toString());
        }
        return options;
    }

    @Override
    public AppiumDriver createSession(DeviceSpec device) {
        ConfigManager config = ConfigManager.getInstance();
        String serverUrl = config.appiumServerUrl();
        if (serverUrl == null || serverUrl.isBlank()) {
            throw new ConfigException("appium.server.url is required for the local provider");
        }
        try {
            return new AndroidDriver(URI.create(serverUrl).toURL(), buildCapabilities(device));
        } catch (MalformedURLException exception) {
            throw new ConfigException("Invalid Appium server URL '" + serverUrl + "'", exception);
        }
    }
}
