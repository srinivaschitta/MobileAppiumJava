package com.saucedemo.mobile.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private ReportManager() {
    }

    public static void startReport() {
        if (extentReports != null) {
            return;
        }
        ExtentSparkReporter spark = new ExtentSparkReporter("target/extent/index.html");
        spark.config().setDocumentTitle("My Demo App mobile suite");
        spark.config().setReportName("Appium + TestNG");
        extentReports = new ExtentReports();
        extentReports.attachReporter(spark);
    }

    public static void startTest(String testName, String description) {
        CURRENT_TEST.set(extentReports.createTest(testName, description));
    }

    public static void logStep(String message) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.log(Status.INFO, message);
        }
    }

    public static void markPassed(String message) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.pass(message);
        }
    }

    public static void markFailed(Throwable throwable) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.fail(throwable);
        }
    }

    public static void markSkipped(String message) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.skip(message);
        }
    }

    public static void attachScreenshot(Path screenshotFile) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null && screenshotFile != null && Files.exists(screenshotFile)) {
            currentTest.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotFile.toAbsolutePath().toString()).build());
        }
    }

    public static void attachPlainText(String title, String content) {
        if (content == null) {
            return;
        }
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.info(title + ":<br><pre>" + content + "</pre>");
        }
    }

    public static void attachXmlFile(String title, Path xmlFile) {
        if (xmlFile == null || !Files.exists(xmlFile)) {
            return;
        }
        try {
            attachPlainText(title, Files.readString(xmlFile, StandardCharsets.UTF_8));
        } catch (Exception ignored) {
            attachPlainText(title, xmlFile.toString());
        }
    }

    public static void writeReportToDisk() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static void clearCurrentTest() {
        CURRENT_TEST.remove();
    }
}
