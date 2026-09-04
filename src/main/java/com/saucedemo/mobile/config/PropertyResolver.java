package com.saucedemo.mobile.config;

import java.util.Properties;

final class PropertyResolver {

    private final Properties fileProperties;

    PropertyResolver(Properties fileProperties) {
        this.fileProperties = fileProperties;
    }

    String readRequiredProperty(String propertyKey, String environmentVariableName) {
        String propertyValue = readOptionalProperty(propertyKey, environmentVariableName);
        if (!hasText(propertyValue)) {
            throw new ConfigException("Missing required property '" + propertyKey + "'");
        }
        return propertyValue;
    }

    String readOptionalProperty(String propertyKey, String environmentVariableName) {
        return firstNonBlank(
                System.getProperty(propertyKey),
                System.getenv(environmentVariableName),
                fileProperties.getProperty(propertyKey));
    }

    long readRequiredPositiveNumber(String propertyKey, String environmentVariableName) {
        String rawValue = readRequiredProperty(propertyKey, environmentVariableName);
        try {
            long parsed = Long.parseLong(rawValue);
            if (parsed <= 0) {
                throw new ConfigException("Property '" + propertyKey + "' must be a positive number");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new ConfigException("Property '" + propertyKey + "' is not a number: " + rawValue, exception);
        }
    }

    static String firstNonBlank(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (String candidate : candidates) {
            if (hasText(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
