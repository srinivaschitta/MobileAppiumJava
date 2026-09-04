package com.saucedemo.mobile.diagnostics;

import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FailureDiagnostics {

    private static final Logger log = LoggerFactory.getLogger(FailureDiagnostics.class);

    private FailureDiagnostics() {
    }

    public static DiagnosticBundle capture(ITestResult result) {
        Path directory = Path.of(
                "target",
                "diagnostics",
                result.getTestClass().getName() + "." + result.getMethod().getMethodName());
        DiagnosticBundle bundle = new DiagnosticBundle(directory);
        try {
            Files.createDirectories(directory);
            AppiumDriver driver = DriverManager.getOrNull();
            if (driver == null) {
                write(directory.resolve("session.txt"), "No driver bound to this thread.");
                return bundle;
            }
            captureScreenshot(driver, directory.resolve("screenshot.png"), bundle);
            capturePageSource(driver, directory.resolve("page-source.xml"), bundle);
            write(directory.resolve("session.txt"), sessionMeta(driver));
            bundle.sessionText = sessionMeta(driver);
            captureLogcat(driver, directory.resolve("logcat.txt"), bundle);
            writeCloudJobUrl(driver, directory.resolve("cloud-job.url"), bundle);
        } catch (Exception exception) {
            log.warn("Failed to capture diagnostics", exception);
        }
        return bundle;
    }

    private static void captureScreenshot(AppiumDriver driver, Path file, DiagnosticBundle bundle) {
        try {
            Files.write(file, driver.getScreenshotAs(OutputType.BYTES));
            bundle.screenshot = file;
        } catch (Exception exception) {
            log.warn("Screenshot capture failed", exception);
        }
    }

    private static void capturePageSource(AppiumDriver driver, Path file, DiagnosticBundle bundle) {
        try {
            write(file, driver.getPageSource());
            bundle.pageSource = file;
        } catch (Exception exception) {
            log.warn("Page source capture failed", exception);
        }
    }

    private static void captureLogcat(AppiumDriver driver, Path file, DiagnosticBundle bundle) {
        if (!(driver instanceof AndroidDriver androidDriver)) {
            return;
        }
        try {
            List<org.openqa.selenium.logging.LogEntry> entries = androidDriver.manage().logs().get("logcat").getAll();
            StringBuilder builder = new StringBuilder();
            int start = Math.max(0, entries.size() - 200);
            for (int i = start; i < entries.size(); i++) {
                builder.append(entries.get(i).getMessage()).append(System.lineSeparator());
            }
            write(file, builder.toString());
            bundle.logcat = file;
        } catch (Exception exception) {
            log.debug("logcat is not available on this session");
        }
    }

    private static void writeCloudJobUrl(AppiumDriver driver, Path file, DiagnosticBundle bundle) {
        if (!"sauce".equals(ConfigManager.getInstance().provider())) {
            return;
        }
        try {
            String sessionId = String.valueOf(driver.getSessionId());
            String url = "https://app.saucelabs.com/tests/" + sessionId;
            write(file, url);
            bundle.cloudJobUrl = url;
        } catch (Exception exception) {
            log.debug("Could not resolve Sauce job URL");
        }
    }

    private static String sessionMeta(AppiumDriver driver) {
        ConfigManager config = ConfigManager.getInstance();
        return "provider=" + config.provider()
                + System.lineSeparator()
                + "env=" + config.environmentName()
                + System.lineSeparator()
                + "deviceProfile=" + config.deviceProfile()
                + System.lineSeparator()
                + "sessionId=" + driver.getSessionId();
    }

    private static void write(Path file, String content) throws Exception {
        Files.writeString(file, content == null ? "" : content, StandardCharsets.UTF_8);
    }

    public static final class DiagnosticBundle {
        public final Path directory;
        public Path screenshot;
        public Path pageSource;
        public Path logcat;
        public String sessionText;
        public String cloudJobUrl;

        private DiagnosticBundle(Path directory) {
            this.directory = directory;
        }
    }
}
