package com.saucedemo.mobile.provider;

import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.config.DeviceSpec;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.MutableCapabilities;

public final class BrowserStackProvider implements ExecutionProvider {

    @Override
    public String providerName() {
        return "browserstack";
    }

    @Override
    public MutableCapabilities buildCapabilities(DeviceSpec device) {
        ConfigManager config = ConfigManager.getInstance();
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", config.platformName());
        capabilities.setCapability("appium:automationName", config.automationName());
        capabilities.setCapability("appium:app", config.appPath());

        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("userName", config.browserstackUsername());
        browserstackOptions.setCapability("accessKey", config.browserstackAccessKey());
        browserstackOptions.setCapability("deviceName", device.deviceName());
        browserstackOptions.setCapability("osVersion", device.platformVersion());
        browserstackOptions.setCapability("projectName", "My Demo App");
        browserstackOptions.setCapability("buildName", config.buildName());
        capabilities.setCapability("bstack:options", browserstackOptions);
        return capabilities;
    }

    @Override
    public AppiumDriver createSession(DeviceSpec device) {
        throw new UnsupportedOperationException(
                "BrowserStack provider is a compiling stub. Implement createSession when an app id and credentials are available.");
    }
}
