package com.saucedemo.mobile.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public final class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();

    private final String environmentName;
    private final String provider;
    private final String appiumServerUrl;
    private final String appPath;
    private final String appPackage;
    private final String appActivity;
    private final String appWaitActivity;
    private final String platformName;
    private final String automationName;
    private final Duration explicitWaitTimeout;
    private final Duration waitPollingInterval;
    private final String deviceProfile;
    private final String appResetMode;
    private final String sauceDatacenter;
    private final String buildName;
    private final String sauceUsername;
    private final String sauceAccessKey;
    private final String browserstackUsername;
    private final String browserstackAccessKey;

    private ConfigManager() {
        this.environmentName = PropertyResolver.firstNonBlank(
                System.getenv("MOBILE_ENV"), System.getProperty("env"), "local");
        PropertyResolver properties = new PropertyResolver(loadEnvironmentFile(environmentName));
        this.provider = properties.readRequiredProperty("provider", "MOBILE_PROVIDER");
        this.appiumServerUrl = properties.readOptionalProperty("appium.server.url", "APPIUM_SERVER_URL");
        this.appPath = properties.readRequiredProperty("app.path", "MOBILE_APP_PATH");
        this.appPackage = properties.readRequiredProperty("app.package", "MOBILE_APP_PACKAGE");
        this.appActivity = properties.readRequiredProperty("app.activity", "MOBILE_APP_ACTIVITY");
        this.appWaitActivity = PropertyResolver.firstNonBlank(
                properties.readOptionalProperty("app.wait.activity", "MOBILE_APP_WAIT_ACTIVITY"),
                appActivity);
        this.platformName = properties.readRequiredProperty("platform.name", "MOBILE_PLATFORM_NAME");
        this.automationName = properties.readRequiredProperty("automation.name", "MOBILE_AUTOMATION_NAME");
        this.explicitWaitTimeout = Duration.ofSeconds(
                properties.readRequiredPositiveNumber("wait.timeout.seconds", "MOBILE_WAIT_TIMEOUT_SECONDS"));
        this.waitPollingInterval = Duration.ofMillis(
                properties.readRequiredPositiveNumber("wait.polling.millis", "MOBILE_WAIT_POLLING_MILLIS"));
        this.deviceProfile = properties.readRequiredProperty("device.profile", "MOBILE_DEVICE_PROFILE");
        this.appResetMode = properties.readRequiredProperty("app.reset.mode", "MOBILE_APP_RESET_MODE");
        this.sauceDatacenter = properties.readOptionalProperty("sauce.datacenter", "SAUCE_DATACENTER");
        this.buildName = PropertyResolver.firstNonBlank(
                System.getenv("GITHUB_SHA"),
                properties.readOptionalProperty("build.name", "MOBILE_BUILD_NAME"),
                "my-demo-app-mobile");
        this.sauceUsername = PropertyResolver.firstNonBlank(
                System.getenv("SAUCE_USERNAME"), properties.readOptionalProperty("sauce.username", "SAUCE_USERNAME"));
        this.sauceAccessKey = PropertyResolver.firstNonBlank(
                System.getenv("SAUCE_ACCESS_KEY"), properties.readOptionalProperty("sauce.access.key", "SAUCE_ACCESS_KEY"));
        this.browserstackUsername = PropertyResolver.firstNonBlank(
                System.getenv("BROWSERSTACK_USERNAME"),
                properties.readOptionalProperty("browserstack.username", "BROWSERSTACK_USERNAME"));
        this.browserstackAccessKey = PropertyResolver.firstNonBlank(
                System.getenv("BROWSERSTACK_ACCESS_KEY"),
                properties.readOptionalProperty("browserstack.access.key", "BROWSERSTACK_ACCESS_KEY"));
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public String environmentName() {
        return environmentName;
    }

    public String provider() {
        return provider;
    }

    public String appiumServerUrl() {
        return appiumServerUrl;
    }

    public String appPath() {
        return appPath;
    }

    public String appPackage() {
        return appPackage;
    }

    public String appActivity() {
        return appActivity;
    }

    public String appWaitActivity() {
        return appWaitActivity;
    }

    public String platformName() {
        return platformName;
    }

    public String automationName() {
        return automationName;
    }

    public Duration explicitWaitTimeout() {
        return explicitWaitTimeout;
    }

    public Duration waitPollingInterval() {
        return waitPollingInterval;
    }

    public String deviceProfile() {
        return deviceProfile;
    }

    public String appResetMode() {
        return appResetMode;
    }

    public String sauceDatacenter() {
        return sauceDatacenter;
    }

    public String buildName() {
        return buildName;
    }

    public String sauceUsername() {
        return sauceUsername;
    }

    public String sauceAccessKey() {
        return sauceAccessKey;
    }

    public String browserstackUsername() {
        return browserstackUsername;
    }

    public String browserstackAccessKey() {
        return browserstackAccessKey;
    }

    public boolean hasSauceCredentials() {
        return PropertyResolver.hasText(sauceUsername) && PropertyResolver.hasText(sauceAccessKey);
    }

    public boolean hasBrowserstackCredentials() {
        return PropertyResolver.hasText(browserstackUsername) && PropertyResolver.hasText(browserstackAccessKey);
    }

    private static Properties loadEnvironmentFile(String selectedEnvironment) {
        String classpathLocation = "config/" + selectedEnvironment + ".properties";
        try (InputStream propertiesStream = ConfigManager.class.getClassLoader().getResourceAsStream(classpathLocation)) {
            if (propertiesStream == null) {
                throw new ConfigException(
                        "Unknown env '" + selectedEnvironment + "': classpath resource '"
                                + classpathLocation + "' was not found");
            }
            Properties fileProperties = new Properties();
            fileProperties.load(propertiesStream);
            return fileProperties;
        } catch (IOException ioException) {
            throw new ConfigException("Failed to read '" + classpathLocation + "'", ioException);
        }
    }
}
