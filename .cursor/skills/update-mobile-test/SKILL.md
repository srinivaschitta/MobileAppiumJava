---
name: update-mobile-test
description: Extends an existing Appium TestNG test or screen method without adding Utils classes or PageFactory. Use when the user asks to update or extend a mobile UI test.
---

# Update mobile test

1. Prefer changing the existing `@Test` or adding a sibling `validate…` method in the same class.
2. New UI steps go on the existing screen (or a small component like `Header` / `ProductCard`).
3. Do not introduce `Utils`, `GenericFunctions`, or extra driver wrappers. Extend `BaseScreen` if the wait/gesture is reusable.
4. Keep tests thin: journey + assert only. No locators, sleeps, or report calls. The listener owns failure artifacts.
5. Keep independence: do not chain tests or rely on cart/login leftover from another method. `BaseTest` resets app state.
