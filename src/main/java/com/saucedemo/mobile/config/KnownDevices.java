package com.saucedemo.mobile.config;

public final class KnownDevices {

    private KnownDevices() {
    }

    public static DeviceSpec findByProfileName(String profileName) {
        return switch (profileName) {
            case "local-pixel7" -> new DeviceSpec("local-pixel7", "Pixel_7_Pro_API_36", "16");
            case "sauce-pixel7" -> new DeviceSpec("sauce-pixel7", "Google Pixel 7 Pro GoogleAPI Emulator", "14.0");
            case "browserstack-pixel7" -> new DeviceSpec("browserstack-pixel7", "Google Pixel 7", "13.0");
            default -> throw new ConfigException("Unknown device profile '" + profileName + "'");
        };
    }
}
