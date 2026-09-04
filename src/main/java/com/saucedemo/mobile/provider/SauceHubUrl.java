package com.saucedemo.mobile.provider;

import com.saucedemo.mobile.config.ConfigException;

import java.net.URI;

public final class SauceHubUrl {

    private SauceHubUrl() {
    }

    public static URI resolve(String datacenter) {
        if (datacenter == null || datacenter.isBlank()) {
            throw new ConfigException("sauce.datacenter is required for the Sauce Labs provider");
        }
        return switch (datacenter) {
            case "us-west-1" -> URI.create("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
            case "eu-central-1" -> URI.create("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
            default -> throw new ConfigException("Unsupported Sauce datacenter '" + datacenter + "'");
        };
    }
}
