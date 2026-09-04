---
name: heal-mobile-test
description: Assisted healing for failing Appium TestNG tests. Use when Surefire fails, a locator is stale, or My Demo App UI drift is suspected. Never auto-commit.
---

# Heal mobile test (assisted)

This is not runtime locator healing and not a watcher that rewrites tests on failure.

1. Read the failing test, Surefire XML/txt under `target/surefire-reports`, and `target/diagnostics/<class>.<method>/` (screenshot, page-source, session.txt).
2. Use filesystem MCP only if it is rooted at this repo. Use Appium MCP `generate_locators` / `appium_find_element` only when a live session is available.
3. If an accessibility id changed, propose an update to the `By` constant on the screen. Keep locator priority: accessibility id → id → UiAutomator → XPath.
4. If the failure is timing, propose a wait change in `BaseScreen` / config timeout — not `Thread.sleep` in the test.
5. Show the proposed diff and wait for review. Never auto-commit. Never silently edit passing tests.
