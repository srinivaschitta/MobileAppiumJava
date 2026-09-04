package com.saucedemo.mobile.config;

public final class DeviceSpec {

    private final String profileName;
    private final String deviceName;
    private final String platformVersion;

    public DeviceSpec(String profileName, String deviceName, String platformVersion) {
        this.profileName = profileName;
        this.deviceName = deviceName;
        this.platformVersion = platformVersion;
    }

    public String profileName() {
        return profileName;
    }

    public String deviceName() {
        return deviceName;
    }

    public String platformVersion() {
        return platformVersion;
    }
}
