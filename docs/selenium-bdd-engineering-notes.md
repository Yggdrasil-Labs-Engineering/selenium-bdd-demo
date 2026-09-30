# Selenium BDD Automation - Engineering Notes

## Purpose

This document provides implementation and handoff notes for the Selenium BDD automation demonstration project.

The project is intentionally small and is designed to demonstrate:

- Java-based UI automation
- Selenium WebDriver
- Cucumber / Gherkin BDD scenarios
- JUnit assertions
- positive and negative workflow validation
- business-rule validation
- reusable test steps
- clear PASS / FAIL execution through Maven

## Current Implementation

The project automates workflows against SauceDemo using:

- Java 21
- Maven
- Selenium WebDriver
- JUnit Jupiter
- Cucumber
- Gherkin feature files
- Maven Surefire

The test suite includes both direct Selenium/JUnit smoke tests and Cucumber-driven workflow scenarios.

## Architecture

```text
Gherkin Feature File
        ↓
Cucumber Step Definitions
        ↓
Java
        ↓
Selenium WebDriver
        ↓
SauceDemo UI
        ↓
Assertions
        ↓
PASS / FAIL

### Project Structure

selenium-bdd-demo/
├── pom.xml
├── src/
│   └── test/
│       ├── java/
│       │   ├── RunCucumberTest.java
│       │   ├── SauceDemoSmokeTest.java
│       │   └── steps/
│       │       └── LoginSteps.java
│       └── resources/
│           └── features/
│               └── login.feature
└── docs/
    └── selenium-bdd-engineering-notes.md

### Test Execution

Run the complete test suite with: mvn clean test

A successful run should complete with: **BUILD** **SUCCESS**

The suite currently executes:
- direct Selenium/JUnit smoke tests
- Cucumber **BDD** workflow scenarios

### Current Test Coverage
### Basic Browser Validation
The direct Selenium tests validate that:
- Chrome launches successfully
- SauceDemo is reachable
- the expected page loads
- a valid user can log in
- navigation to the inventory page succeeds
**BDD** Workflow Coverage
The Cucumber feature file currently covers the following business workflows.

### Successful Login
Scenario: Successful login
    Given I am on the SauceDemo login page
    When I log in with valid credentials
    Then I should see the inventory page

This verifies the valid authentication path and confirms successful navigation into the application.

### Invalid Login
Scenario: Reject invalid login credentials
    Given I am on the SauceDemo login page
    When I log in with invalid credentials
    Then I should see a login error message

This validates that invalid credentials are rejected and the expected user-facing error is displayed.
Add Product to Cart
Scenario: Add a product to the cart
    When I add the Sauce Labs Backpack to the cart
    Then the cart should contain 1 item

This verifies that:
- the selected product can be added
- the cart state updates
- the cart badge reflects the expected item count
Remove Product from Cart
Scenario: Remove a product from the cart
    Given I have added the Sauce Labs Backpack to the cart
    When I remove the Sauce Labs Backpack from the cart
    Then the cart should be empty

This verifies that:
- an existing cart item can be removed
- the cart state updates correctly
- the cart badge disappears when no items remain

### Checkout Total Validation
Scenario: Validate checkout totals
    Given I have added the Sauce Labs Backpack to the cart
    When I proceed through checkout with valid customer information
    Then the item subtotal should match the product price
    And the final total should equal subtotal plus tax

This scenario moves beyond simple UI interaction and validates business calculations. The test reads values from the UI and verifies: Product Price = Item Subtotal Item Subtotal + Tax = Final Total

A tolerance of 0.01 is used for numeric comparison.

### Missing Checkout Information
Scenario: Prevent checkout when required customer information is missing
    Given I have added the Sauce Labs Backpack to the cart
    When I attempt checkout without entering customer information
    Then I should see a checkout validation error

This verifies that:
- required customer information is enforced
- the workflow does not proceed with incomplete data
- the expected validation message is displayed

### Background Step
The feature file uses a shared login background so that applicable scenarios begin from a known authenticated state.
Example:
Background:
    Given I am on the SauceDemo login page
    When I log in with valid credentials
    Then I should see the inventory page

This reduces duplication and keeps scenario intent readable. ### Step Reuse Cucumber step definitions are intentionally reused across scenarios. For example: Given I have added the Sauce Labs Backpack to the cart

is used by multiple scenarios instead of duplicating Java code.
This keeps the automation easier to maintain and reduces unnecessary implementation repetition.

### Browser Lifecycle
Each scenario creates a fresh browser session.
The browser is closed after execution using the Cucumber @After hook.
Conceptually:
Scenario starts
      ↓
Chrome session created
      ↓
Workflow executed
      ↓
Assertions evaluated
      ↓
driver.quit()

This helps keep scenarios isolated and reduces state contamination between test runs.
Assertions
JUnit assertions are used to validate expected behavior.
Current assertion types include:
- page **URL** validation
- cart count validation
- cart-empty validation
- error-message validation
- subtotal validation
- tax / final-total validation
The automation is designed to validate observable business behavior rather than only confirm that UI controls can be clicked.

### Dependency Management
Dependencies are managed through Maven in pom.xml.
The current working dependency family includes:
Selenium       4.49.0
Cucumber       7.20.1
JUnit Jupiter  5.11.2
JUnit Platform 1.11.2
Surefire       3.5.4

### Dependency Compatibility Note

During initial setup, incompatible JUnit Platform versions caused test-discovery failures. Symptoms included: TestEngine with ID 'junit-jupiter' failed to discover tests

and: NoSuchMethodError

The root cause was version skew between JUnit Platform dependencies introduced through Cucumber and JUnit. The issue was resolved by aligning the JUnit Jupiter and JUnit Platform versions with the version expected by the selected Cucumber release. This is an important maintenance consideration when upgrading framework dependencies. ### Known Environment Warning During Chrome execution, Selenium currently reports a Chrome DevTools Protocol compatibility warning. Observed behavior: Unable to find an exact match for **CDP** version **154**. Returning the closest version: **153**.

Current impact:
- test execution succeeds
- browser automation functions correctly
- no test failures have been attributed to the warning
The warning should be revisited when upgrading Selenium or Chrome.

### Design Decisions
**BDD** / Gherkin
Gherkin is used to keep workflow intent readable to both technical and non-technical stakeholders.
Example:
Then the final total should equal subtotal plus tax

This communicates the business rule without exposing implementation details. Separate Business Intent from Automation Logic Feature files describe expected behavior. Java step definitions contain the Selenium implementation. This separation keeps business scenarios readable and implementation details maintainable. ### Small Scenario Scope Scenarios are intentionally narrow and focused on one business behavior at a time. This makes failures easier to diagnose and reduces ambiguity. Positive and Negative Coverage The project includes both: Expected success paths

and: Expected rejection / validation paths

A negative test passes when the application correctly rejects invalid input or prevents an invalid workflow from continuing. Troubleshooting ### Cucumber Undefined Step Example symptom: UndefinedStep

Check that the Gherkin step text exactly matches the corresponding Java annotation. Cucumber step matching is text-sensitive. Example: When I remove the Sauce Labs Backpack from the cart

must match: @When(*I remove the Sauce Labs Backpack from the cart*)

Differences in capitalization or wording can prevent step resolution. ### Maven Does Not Discover Tests Verify that Java test files are under: src/test/java

and feature files are under: src/test/resources

Maven will not discover test classes stored outside the configured test-source structure.

### Browser Does Not Launch
Verify:
- Chrome is installed
- Java is available
- Maven dependencies restore successfully
- Selenium Manager can resolve the required browser driver

### Current Limitations
This project is a demonstration and does not currently include:
- Page Object Model abstraction
- configurable test data
- externalized credentials
- CI/CD execution
- screenshots on failure
- structured test reporting
- parallel execution
- cross-browser execution
- retry handling
- test tags / suites
- data-driven scenario outlines
These would be reasonable additions for a production automation framework.

### Future Enhancements
Potential extensions include:
- Page Object Model
- CI pipeline execution
- Firefox / Edge coverage
- screenshot capture on failure
- richer reporting
- scenario tagging
- reusable test-data configuration
- additional checkout workflows
- discount / pricing-rule testing against a suitable test application
- downstream data validation

### Handoff Summary
This project demonstrates a small Java Selenium automation framework using Cucumber **BDD** and JUnit.
The current suite validates authentication, cart behavior, checkout calculations, required-field validation, and both positive and negative paths.
The implementation is intentionally compact so that the relationship between business-readable Gherkin scenarios and Selenium automation remains easy to understand, debug, and extend.