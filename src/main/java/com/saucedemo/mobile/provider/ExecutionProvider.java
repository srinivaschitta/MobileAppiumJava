package com.saucedemo.mobile.provider;

import com.saucedemo.mobile.config.DeviceSpec;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Capabilities;

public interface ExecutionProvider {

    String providerName();

    Capabilities buildCapabilities(DeviceSpec device);

    AppiumDriver createSession(DeviceSpec device);
}
