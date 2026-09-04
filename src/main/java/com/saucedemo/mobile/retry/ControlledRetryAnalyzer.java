package com.saucedemo.mobile.retry;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.net.ConnectException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class ControlledRetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(ControlledRetryAnalyzer.class);
    private static final int MAX_RETRIES = 1;
    private static final ConcurrentHashMap<String, AtomicInteger> RETRY_COUNTS = new ConcurrentHashMap<>();
    private static final AtomicInteger SUITE_RETRY_TOTAL = new AtomicInteger();

    @Override
    public boolean retry(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (!isInfraFailure(throwable)) {
            return false;
        }
        String key = result.getTestClass().getName() + "#" + result.getMethod().getMethodName();
        int attempts = RETRY_COUNTS.computeIfAbsent(key, ignored -> new AtomicInteger()).incrementAndGet();
        if (attempts > MAX_RETRIES) {
            return false;
        }
        SUITE_RETRY_TOTAL.incrementAndGet();
        log.warn("Retrying {} once after infra failure: {}", key, throwable.getClass().getSimpleName());
        return true;
    }

    public static int suiteRetryTotal() {
        return SUITE_RETRY_TOTAL.get();
    }

    private static boolean isInfraFailure(Throwable throwable) {
        if (throwable == null || throwable instanceof AssertionError) {
            return false;
        }
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof SessionNotCreatedException || current instanceof ConnectException) {
                return true;
            }
            if (current instanceof TimeoutException && messageContains(current, "Could not start a new session")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private static boolean messageContains(Throwable throwable, String fragment) {
        return throwable.getMessage() != null && throwable.getMessage().contains(fragment);
    }
}
