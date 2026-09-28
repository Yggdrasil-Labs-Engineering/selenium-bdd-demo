import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SauceDemoSmokeTest {

    @Test
    void shouldOpenSauceDemo() {
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.saucedemo.com/");

            assertTrue(
                    driver.getTitle().contains("Swag Labs"),
                    "Expected SauceDemo page title to contain 'Swag Labs'");
        } finally {
            driver.quit();
        }
    }
    @Test
    void shouldLoginSuccessfully() {
        WebDriver driver = new ChromeDriver();

    try {
        driver.get("https://www.saucedemo.com/");

        driver.findElement(By.id("user-name"))
              .sendKeys("standard_user");

        driver.findElement(By.id("password"))
              .sendKeys("secret_sauce");

        driver.findElement(By.id("login-button"))
              .click();

        assertTrue(
            driver.getCurrentUrl().contains("inventory"),
            "Expected login to navigate to the inventory page"
        );

    } finally {
        driver.quit();
    }
}


}