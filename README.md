# My Demo App — Appium + Java

Android UI suite for Sauce Labs **My Demo App**. It covers login, catalog, cart, and checkout.

This repo is the framework plus a few sample tests. A full device farm is out of scope. Running the UI tests needs a local Appium 2 server and an Android emulator, or Sauce Labs credentials.

## Design

Stack: Java 17, Maven, TestNG, Appium Java Client 9, screen objects, Log4j2, Extent Reports.

Choices that matter:

- Screens do not extend the test base class. Tests start and stop the driver. Screens only talk to the UI.
- Tests stay thin. A `*Journey` does the clicks. A `*Assert` class checks the result.
- Demo users and products are Java classes (`DemoUser.standardUser()`)
- The driver is stored in `ThreadLocal`, so parallel runs do not share a session.
- Local, Sauce Labs, and BrowserStack go through the same `ExecutionProvider` interface. Local and Sauce are wired up. BrowserStack builds capabilities but does not start a session yet.
- Appium 2 with W3C options (`UiAutomator2Options`).
- Implicit wait is 0. Every tap and type goes through an explicit wait in `BaseScreen`.
- No PageFactory. Locators sit on the screen as `By` constants.
- Failures are handled in a TestNG listener, not in `tearDown`.

## App
Release : https://github.com/saucelabs/my-demo-app-rn/releases#release-v1.3.0

- Package: `com.saucelabs.mydemoapp.rn`
- Activity: `com.saucelabs.mydemoapp.rn.MainActivity`

This build keeps login and cart data after the app is killed. After each session starts, the suite long-presses the header (`longpress reset app`, 1 second) to clear that state.

Place the APK at `apps/`. It is gitignored. See [apps/README.md](apps/README.md).

## Folders

```
src/main/java/com/saucedemo/mobile/
  config/       env files + KnownDevices
  data/         DemoUser, DemoProduct, DemoCheckoutForm
  driver/       DriverFactory, DriverManager
  provider/     local, Sauce, BrowserStack
  screens/      LoginScreen, CatalogScreen, CartScreen, checkout screens
  journey/      LoginJourney, CartJourney, CheckoutJourney
  listener/     MobileTestListener, RetryTransformer
  retry/        ControlledRetryAnalyzer
  report/       Extent ReportManager
  diagnostics/  screenshot and page source on failure

src/test/java/com/saucedemo/mobile/
  tests/        thin TestNG classes
  assertion/    LoginAssert, CartAssert, CheckoutAssert

src/test/resources/
  config/       local.properties, sauce.properties, browserstack.properties
  testng-*.xml
  log4j2-test.xml
```

## Config

Set the env with `MOBILE_ENV`, or `-Denv`, or leave it. Default is `local`.

Files: `src/test/resources/config/{local,sauce,browserstack}.properties`.

If the same key is set in more than one place, this order wins: `-Dkey`, then an environment variable, then the file.

Do not put Sauce or BrowserStack keys in the properties files. Use `SAUCE_USERNAME` and `SAUCE_ACCESS_KEY`. A missing required key throws `ConfigException`.

Device names live in `KnownDevices` (Java). The env file picks one with `device.profile=local-pixel34`.

## Local vs cloud

- Local: talks to Appium already running on `http://127.0.0.1:4723`. Tests do not start the server.
- Sauce Labs: uses `sauce:options` and the hub for `us-west-1` or `eu-central-1`.
- BrowserStack: capability shape is in place for a later implementation.

## Locators and waits

Accessibility ids come first. UiAutomator or XPath is used only when a product row must be found by name.

Waits and gestures live in `BaseScreen`: `tap`, `type`, `scrollTo`, `isDisplayed`.

Ids live on each `*Screen` class as `private static final By` constants.

## Naming

- Tests: `validateLoginSucceedsWithValidCredentials`
- Screens: `LoginScreen`
- Journeys: `LoginJourney.loginWith(...)`
- Asserts: `LoginAssert.assertCatalogIsShownAfterLogin(...)`
- Config helpers: `readRequiredProperty`, `readOptionalProperty` — not `required` / `read`
- Driver: `createExecutionProvider` — not `providerFor`
- Locator constants: `LOGIN_BUTTON`

## Listeners, retry, and failure analysis

Two TestNG listeners are registered in every `testng-*.xml`:

1. **`MobileTestListener`** — suite start/stop and each test result.
   - Suite start: open the Extent report (`target/extent/index.html`).
   - Test start: create a report node.
   - Pass: mark pass.
   - Fail: mark fail, then `FailureDiagnostics` writes `target/diagnostics/<class>.<method>/`:
     - `screenshot.png`
     - `page-source.xml`
     - `session.txt` (env, provider, session id)
     - `logcat.txt` when the Android log is available
     - `cloud-job.url` on Sauce (`https://app.saucelabs.com/tests/<sessionId>`)
   - Those files are also attached to the Extent test.
   - Suite end: write the HTML report and `target/retry-summary.txt`.

2. **`RetryTransformer`** — attaches `ControlledRetryAnalyzer` to every `@Test`.
   - Retries **once**.
   - Only for infra failures: `SessionNotCreatedException`, connection errors, or “could not start a new session”.
   - Assertion failures are not retried.
   - A retried test is marked skip on the first attempt, then run again.

`BaseTest` only creates and quits the driver. It does not take screenshots.

## Sample tests

Five tests. Each one is arrange (fixtures), act (journey), assert.

| Test | Check |
|---|---|
| `validateLoginSucceedsWithValidCredentials` | Catalog is shown |
| `validateLoginRejectedWithWrongPassword` | Error stays on login |
| `validateLoginRejectedForLockedUser` | Locked message for `alice@example.com` |
| `validateCatalogItemAppearsInCart` | Backpack is in the cart |
| `validateCheckoutCompletesWithDemoAddress` | Order complete screen |

Users live in `DemoUser`: standard is `bob@example.com` / `10203040`. Locked is `alice@example.com`.

## How to run

Local needs **two** processes. The emulator and Appium must already be up. Tests do not start either one.

1. Start the Pixel 7 Pro AVD from Android Studio (or `emulator -avd Pixel_7_Pro_API_36`).
2. In a dedicated terminal, start Appium on the URL in `local.properties`:

```bash
appium --address 127.0.0.1 --port 4723
```

Leave that process running. `Appium REST http interface listener started on 127.0.0.1:4723` means it is ready.

3. In another terminal, run the suite:

```bash
mvn test -Denv=local
```

`SessionNotCreatedException` / `ConnectException` almost always means Appium is not listening on `http://127.0.0.1:4723`. The emulator and APK alone are not enough.

Sauce Labs (upload the APK to Sauce Storage first):

```bash
export SAUCE_USERNAME=...
export SAUCE_ACCESS_KEY=...
mvn test -Denv=sauce -DsuiteXmlFile=src/test/resources/testng-sauce.xml
```

CI compiles on every PR (`mvn test-compile`). It does not need a device. The Sauce job is optional: trigger it by hand or on `main` when secrets are set.


## AI assistance

AI (Cursor) was used on this repo.

## Cursor rules and skills

`.cursor/rules` and `.cursor/skills` tell a Cursor agent how to add tests in this repo (thin tests, screen objects, no silent locator rewrite). They are editor help only. Maven does not read them.

## Appium MCP — what it is

MCP here means a Cursor plugin that can talk to a phone. It is **not** a Java library and it is **not** on the Maven classpath.

There are two separate ways to talk to My Demo App:

| | Java suite (`mvn test`) | Appium MCP (Cursor chat) |
|---|---|---|
| Who uses it | You, CI, reviewers | You + the agent, while writing code |
| Talks to | Appium server / Sauce | A local emulator through the Appium MCP plugin |
| Lives in | This repo (`*Screen`, `*Tests`) | Cursor (already set up as Appium MCP) |
| In a test run? | Yes | No |

They never call each other. `LoginTests` does not import MCP. MCP does not start TestNG.

### Why it exists

Writing a screen object needs a stable id, for example `Username input field`. You can find that id with Appium Inspector, or you can ask Cursor to look at the live app.

Typical writing flow:

1. Start a local emulator and have the APK installed.
2. In Cursor, ask to inspect the login screen.
3. The agent uses Appium MCP to open a session and list elements.
4. You (or the agent) copy one accessibility id into `LoginScreen`:

```java
private static final By USERNAME_INPUT = AppiumBy.accessibilityId("Username input field");
```

5. `mvn test` then uses that Java locator. MCP is no longer involved.

If MCP sketches a full click-script, do not commit it. Turn the steps into a journey and a screen method.

### When a test fails

1. Open `target/diagnostics/<class>.<method>/` — screenshot and page source (written by `MobileTestListener`).
2. Optionally ask Cursor to open the app again with Appium MCP and confirm the id still exists.
3. Change the `By` constant or the wait. Review the diff before commit.

The suite does not auto-heal locators at runtime.

### Filesystem MCP

`.cursor/mcp.json` adds a second plugin that can only read files inside this repo (Extent HTML, diagnostics, Surefire). It cannot drive the phone. Use it when asking Cursor to explain a failed run.

**Where MCP played a role**

- Appium MCP did **not** generate the committed tests. `appium_generate_tests` was not used as the source of `LoginTests` / `CartTests` / `CheckoutTests`.
- Locator ids were copied from the official My Demo App test helpers, not from a live `generate_locators` session. They still need a check on a real emulator; the ids in use are the `By` constants on each `*Screen` class.
- Filesystem MCP is configured for this repo only. It was not used to generate code.

**What a person still owns**

- Design choices (no PageFactory, no screens extending `BaseTest`, Extent only, Java fixtures instead of JSON).
- Any locator change after a live inspector or MCP pass.
- Saying the suite has not been claimed as green on an emulator.

If MCP is used later to refresh locators, copy the ids into the `*Screen` classes and add a line here.

