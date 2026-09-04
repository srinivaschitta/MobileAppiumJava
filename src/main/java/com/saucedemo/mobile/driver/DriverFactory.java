package com.saucedemo.mobile.driver;

import com.saucedemo.mobile.config.ConfigException;
import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.config.KnownDevices;
import com.saucedemo.mobile.config.DeviceSpec;
import com.saucedemo.mobile.provider.BrowserStackProvider;
import com.saucedemo.mobile.provider.ExecutionProvider;
import com.saucedemo.mobile.provider.LocalEmulatorProvider;
import com.saucedemo.mobile.provider.SauceLabsProvider;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static ExecutionProvider createExecutionProvider(String providerName) {
        return switch (providerName) {
            case "local" -> new LocalEmulatorProvider();
            case "sauce" -> new SauceLabsProvider();
            case "browserstack" -> new BrowserStackProvider();
            default -> throw new ConfigException("Unknown execution provider '" + providerName + "'");
        };
    }

    public static AppiumDriver createDriver() {
        ConfigManager config = ConfigManager.getInstance();
        DeviceSpec device = KnownDevices.findByProfileName(config.deviceProfile());
        ExecutionProvider provider = createExecutionProvider(config.provider());
        log.info("Creating {} session on profile {}", provider.providerName(), device.profileName());
        AppiumDriver driver = provider.createSession(device);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        DriverManager.set(driver);
        return driver;
    }
}
