Feature: SauceDemo login

  Scenario: Successful login
    Given I am on the SauceDemo login page
    When I log in with valid credentials
    Then I should see the inventory page