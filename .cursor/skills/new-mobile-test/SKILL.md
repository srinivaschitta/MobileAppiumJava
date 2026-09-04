---
name: new-mobile-test
description: Adds a new Appium TestNG test following this project's thin-test and screen-object conventions. Use when the user asks to write, add, or scaffold a new mobile UI test.
---

# New mobile test

1. Put the test in `LoginTests`, `CartTests`, or `CheckoutTests`. Do not create a new test class unless the user asks.
2. Name the method `validate<Behavior>()`. Set `@Test(description, groups)`.
3. Write three steps: arrange fixtures, act with a `*Journey`, assert with a `*Assert` class. Do not nest them in one line. Do not put AssertJ or locators in the test method.
4. Reuse `DemoUser`, `DemoProduct`, or `DemoCheckoutForm`. Do not add JSON testdata files.
5. If a new UI action is required, add it to the existing `*Screen` or journey. Do not put `AppiumBy` in the test.
6. Runtime tests must not call Appium MCP. MCP is for locator discovery before writing `By` constants.
