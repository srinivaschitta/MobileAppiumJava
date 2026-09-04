package com.saucedemo.mobile.listener;

import com.saucedemo.mobile.config.ConfigManager;
import com.saucedemo.mobile.diagnostics.FailureDiagnostics;
import com.saucedemo.mobile.report.ReportManager;
import com.saucedemo.mobile.retry.ControlledRetryAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;

public final class MobileTestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LoggerFactory.getLogger(MobileTestListener.class);

    @Override
    public void onStart(ISuite suite) {
        ReportManager.startReport();
        ConfigManager config = ConfigManager.getInstance();
        log.info("Suite '{}' env={} provider={}", suite.getName(), config.environmentName(), config.provider());
    }

    @Override
    public void onFinish(ISuite suite) {
        int retries = ControlledRetryAnalyzer.suiteRetryTotal();
        log.info("Suite '{}' finished. infra retries={}", suite.getName(), retries);
        try {
            Files.writeString(Path.of("target", "retry-summary.txt"), "infraRetries=" + retries);
        } catch (Exception exception) {
            log.debug("Could not write retry summary", exception);
        }
        ReportManager.writeReportToDisk();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ReportManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        ReportManager.logStep("Starting " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportManager.markPassed("Passed");
        ReportManager.clearCurrentTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ReportManager.markFailed(result.getThrowable());
        FailureDiagnostics.DiagnosticBundle bundle = FailureDiagnostics.capture(result);
        ReportManager.attachScreenshot(bundle.screenshot);
        ReportManager.attachXmlFile("page-source", bundle.pageSource);
        if (bundle.sessionText != null) {
            ReportManager.attachPlainText("session", bundle.sessionText);
        }
        if (bundle.cloudJobUrl != null) {
            ReportManager.attachPlainText("sauce-job", bundle.cloudJobUrl);
        }
        ReportManager.clearCurrentTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.wasRetried()) {
            ReportManager.markSkipped("Skipped after infra retry");
        } else {
            ReportManager.markSkipped(result.getThrowable() == null ? "Skipped" : result.getThrowable().getMessage());
        }
        ReportManager.clearCurrentTest();
    }
}
