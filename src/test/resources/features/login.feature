Feature: SauceDemo shopping cart workflow

  Background:
    Given I am on the SauceDemo login page
    When I log in with valid credentials
    Then I should see the inventory page

  Scenario: Add a product to the cart
    When I add the Sauce Labs Backpack to the cart
    Then the cart should contain 1 item

  Scenario: Remove a product from the cart
    Given I have added the Sauce Labs Backpack to the cart
    When I remove the Sauce Labs Backpack from the cart
    Then the cart should be empty

  Scenario: Prevent checkout when required customer information is missing
  Given I have added the Sauce Labs Backpack to the cart
  When I attempt checkout without entering customer information
  Then I should see a checkout validation error  