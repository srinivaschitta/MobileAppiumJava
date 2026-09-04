package com.saucedemo.mobile.screens;

import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.driver.DriverManager;
import com.saucedemo.mobile.report.ReportManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

public abstract class BaseScreen {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final AppiumDriver driver;
    private final Duration timeout;
    private final Duration polling;

    protected BaseScreen() {
        this(DriverManager.get());
    }

    protected BaseScreen(AppiumDriver driver) {
        this.driver = driver;
        ConfigManager config = ConfigManager.getInstance();
        this.timeout = config.explicitWaitTimeout();
        this.polling = config.waitPollingInterval();
    }

    protected WebElement waitUntilVisible(By locator) {
        return waiter().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilClickable(By locator) {
        return waiter().until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitUntilInvisible(By locator) {
        waiter().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void tap(By locator) {
        log.info("Tapping {}", locator);
        waitUntilClickable(locator).click();
    }

    protected void type(By locator, String value) {
        log.info("Typing into {}", locator);
        StaleElementReferenceException lastStale = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                waitUntilClickable(locator).click();
                WebElement field = waitUntilClickable(locator);
                field.clear();
                field.sendKeys(value);
                hideKeyboardIfPresent();
                return;
            } catch (StaleElementReferenceException stale) {
                lastStale = stale;
            }
        }
        throw lastStale;
    }

    protected String textOf(By locator) {
        WebElement element = waitUntilVisible(locator);
        String text = element.getText();
        if (text != null && !text.isBlank()) {
            return text;
        }
        StringBuilder childText = new StringBuilder();
        for (WebElement child : element.findElements(By.className("android.widget.TextView"))) {
            String value = child.getText();
            if (value == null || value.isBlank()) {
                continue;
            }
            if (!childText.isEmpty()) {
                childText.append(' ');
            }
            childText.append(value);
        }
        return childText.toString();
    }

    protected boolean isDisplayed(By locator) {
        return isDisplayed(locator, Duration.ofSeconds(5));
    }

    protected boolean isDisplayed(By locator, Duration timeoutOverride) {
        try {
            new FluentWait<>(driver)
                    .withTimeout(timeoutOverride)
                    .pollingEvery(polling)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException timeoutException) {
            return false;
        }
    }

    protected void scrollTo(By locator) {
        if (isDisplayed(locator, Duration.ofSeconds(2))) {
            return;
        }
        Dimension size = driver.manage().window().getSize();
        for (int attempt = 0; attempt < 6; attempt++) {
            driver.executeScript("mobile: scrollGesture", Map.of(
                    "left", size.getWidth() / 4,
                    "top", size.getHeight() / 4,
                    "width", size.getWidth() / 2,
                    "height", size.getHeight() / 2,
                    "direction", "down",
                    "percent", 0.85));
            if (isDisplayed(locator, Duration.ofSeconds(1))) {
                return;
            }
        }
        waitUntilVisible(locator);
    }

    protected void hideKeyboardIfPresent() {
        if (!(driver instanceof AndroidDriver androidDriver)) {
            return;
        }
        try {
            androidDriver.hideKeyboard();
        } catch (Exception ignored) {
            // RN reports keyboard visibility inconsistently; ignore if already dismissed.
        }
        try {
            if (androidDriver.isKeyboardShown()) {
                androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
            }
        } catch (Exception ignored) {
            // First BACK dismisses the keyboard; ignore if it was already gone.
        }
    }

    protected void step(String message) {
        ReportManager.logStep(message);
        log.info(message);
    }

    private FluentWait<AppiumDriver> waiter() {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(polling)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }
}
