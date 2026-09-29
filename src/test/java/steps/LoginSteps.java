package steps;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private WebDriver driver;

    @Given("I am on the SauceDemo login page")
    public void iAmOnTheSauceDemoLoginPage() {
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @Given("I have added the Sauce Labs Backpack to the cart")
    public void iHaveAddedTheSauceLabsBackpackToTheCart() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack"))
            .click();
    }

    @When("I log in with valid credentials")
    public void iLogInWithValidCredentials() {
        driver.findElement(By.id("user-name"))
                .sendKeys("standard_user");

        driver.findElement(By.id("password"))
                .sendKeys("secret_sauce");

        driver.findElement(By.id("login-button"))
                .click();
    }
    
    @When("I remove the Sauce Labs Backpack from the cart")
    public void iRemoveTheSauceLabsBackpackFromTheCart() {
        driver.findElement(By.id("remove-sauce-labs-backpack"))
                .click();
    }
    
    @When("I add the Sauce Labs Backpack to the cart")
    public void iAddTheSauceLabsBackpackToTheCart() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack"))
                .click();
    }
    
    @When("I proceed through checkout with valid customer information")
    public void iProceedThroughCheckoutWithValidCustomerInformation() {

        driver.findElement(By.className("shopping_cart_link"))
                .click();

        driver.findElement(By.id("checkout"))
                .click();

        driver.findElement(By.id("first-name"))
                .sendKeys("Larry");

        driver.findElement(By.id("last-name"))
                .sendKeys("Luna");

        driver.findElement(By.id("postal-code"))
                .sendKeys("46312");

        driver.findElement(By.id("continue"))
                .click();
    }
    
    @When("I attempt checkout without entering customer information")
    public void iAttemptCheckoutWithoutEnteringCustomerInformation() {

    driver.findElement(By.className("shopping_cart_link"))
          .click();

    driver.findElement(By.id("checkout"))
          .click();

    driver.findElement(By.id("continue"))
          .click();
}

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        assertTrue(
                driver.findElements(By.className("shopping_cart_badge")).isEmpty(),
                "Expected the cart badge to disappear when the cart is empty");
    }
    
    @Then("the cart should contain 1 item")
    public void theCartShouldContainOneItem() {
        String cartCount = driver.findElement(By.className("shopping_cart_badge"))
                             .getText();

        assertEquals("1", cartCount);
    }

    @Then("I should see the inventory page")
    public void iShouldSeeTheInventoryPage() {
        assertTrue(
                driver.getCurrentUrl().contains("inventory"),
                "Expected login to navigate to the inventory page");
    }
    
    @Then("the item subtotal should match the product price")
    public void theItemSubtotalShouldMatchTheProductPrice() {

        String itemPriceText = driver.findElement(
                By.className("inventory_item_price"))
                .getText();

        String subtotalText = driver.findElement(
                By.className("summary_subtotal_label"))
                .getText();

        double itemPrice = Double.parseDouble(
                itemPriceText.replace("$", ""));

        double subtotal = Double.parseDouble(
                subtotalText.replace("Item total: $", ""));

        assertEquals(
                itemPrice,
                subtotal,
                0.01,
                "Expected subtotal to match the product price");
    }
    
    @Then("I should see a checkout validation error")
    public void iShouldSeeACheckoutValidationError() {

    String errorMessage = driver.findElement(
            By.cssSelector("[data-test='error']"))
            .getText();

    assertTrue(
            errorMessage.contains("Error:"),
            "Expected checkout validation error to be displayed"
    );
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}