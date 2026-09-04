package com.saucedemo.mobile.provider;

import com.saucedemo.mobile.config.ConfigException;
import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.config.DeviceSpec;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.MutableCapabilities;

import java.net.MalformedURLException;
import java.net.URI;

public final class SauceLabsProvider implements ExecutionProvider {

    @Override
    public String providerName() {
        return "sauce";
    }

    @Override
    public MutableCapabilities buildCapabilities(DeviceSpec device) {
        ConfigManager config = ConfigManager.getInstance();
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", config.platformName());
        capabilities.setCapability("appium:automationName", config.automationName());
        capabilities.setCapability("appium:deviceName", device.deviceName());
        capabilities.setCapability("appium:platformVersion", device.platformVersion());
        capabilities.setCapability("appium:app", config.appPath());
        capabilities.setCapability("appium:appWaitActivity", config.appWaitActivity());
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:newCommandTimeout", 180);

        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", config.sauceUsername());
        sauceOptions.setCapability("accessKey", config.sauceAccessKey());
        sauceOptions.setCapability("build", config.buildName());
        sauceOptions.setCapability("name", "My Demo App Android");
        sauceOptions.setCapability("appiumVersion", "2.11.0");
        capabilities.setCapability("sauce:options", sauceOptions);
        return capabilities;
    }

    @Override
    public AppiumDriver createSession(DeviceSpec device) {
        ConfigManager config = ConfigManager.getInstance();
        if (!config.hasSauceCredentials()) {
            throw new ConfigException("SAUCE_USERNAME and SAUCE_ACCESS_KEY are required for the Sauce Labs provider");
        }
        URI hub = SauceHubUrl.resolve(config.sauceDatacenter());
        try {
            return new AndroidDriver(hub.toURL(), buildCapabilities(device));
        } catch (MalformedURLException exception) {
            throw new ConfigException("Invalid Sauce Labs hub URL '" + hub + "'", exception);
        }
    }
}
