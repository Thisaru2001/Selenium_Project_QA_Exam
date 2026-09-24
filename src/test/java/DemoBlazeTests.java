
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DemoBlazeTests extends BaseTest {

    private void clickWithRetry(By locator) {
        int retries = 0;
        while (true) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                if (++retries > 3) throw e;
                try { Thread.sleep(500); } catch (InterruptedException ie) {}
            }
        }
    }

    @Test
    public void testHomePageSmoke() {
        System.out.println("--- TC01: Home Page Smoke Test ---");
        
        // Get the page title
        String pageTitle = driver.getTitle();
        System.out.println("Page Title: " + pageTitle);
        
        // Verify the title is not empty
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");
        
        // Locate the PRODUCT STORE heading using ID (non-XPath locator)
        WebElement brandHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nava")));
        
        // Verify PRODUCT STORE is displayed
        boolean isBrandDisplayed = brandHeading.isDisplayed();
        Assert.assertTrue(isBrandDisplayed, "PRODUCT STORE heading should be displayed");
        
        // Print confirmation
        System.out.println("PRODUCT STORE displayed: " + isBrandDisplayed);
        
        
        // Capture evidence
        takeScreenshot("TC01_HomePageSmoke");
    }

    @Test
    public void testSamsungGalaxyS6Selection() {
        System.out.println("--- TC02: Product Selection ---");
        
        // Locate the Phones category using link text
        WebElement phonesCategory = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones")));
        phonesCategory.click();
        
        
        // Locate the Samsung galaxy s6 product using XPath
        clickWithRetry(By.xpath("//a[normalize-space()='" + PRODUCT_SAMSUNG + "']"));
        
        // Verify the product heading is Samsung galaxy s6
        WebElement productHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='name']")));
        String actualHeading = productHeading.getText();
        Assert.assertEquals(actualHeading, PRODUCT_SAMSUNG, "Product heading does not match expected");
        
        // Retrieve the product price
        WebElement productPriceElement = driver.findElement(By.xpath("//h3[@class='price-container']"));
        String productPrice = productPriceElement.getText().split(" ")[0]; // Removing the "*includes tax" part if present
        
        // Print product name and price
        System.out.println("Selected Product: " + actualHeading);
        System.out.println("Product Price: " + productPrice);
        
        
        // Capture evidence
        takeScreenshot("TC02_ProductSelection");
    }

    @Test
    public void testAddSamsungGalaxyS6ToCart() {
        System.out.println("--- TC03: Add Samsung galaxy s6 to Cart ---");
        
        // Select Phones category
        WebElement phonesCategory = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones")));
        phonesCategory.click();
        
        
        // Select Samsung galaxy s6
        clickWithRetry(By.xpath("//a[normalize-space()='" + PRODUCT_SAMSUNG + "']"));
        
        // Click Add to cart
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']")));
        addToCartBtn.click();
        
        // Wait for the JavaScript alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        
        // Read and print the alert text
        String alertText = alert.getText();
        System.out.println("Alert Message: " + alertText);
        
        
        // Accept the alert
        alert.accept();
        
        // Verify that the alert was handled successfully (no alert present anymore)
        Assert.assertTrue(ExpectedConditions.not(ExpectedConditions.alertIsPresent()).apply(driver), "Alert was not handled successfully");
    }

    @Test
    public void testCartManagement() {
        System.out.println("--- TC04: Cart Management ---");
        
        
        // Add Samsung galaxy s6
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
        clickWithRetry(By.xpath("//a[normalize-space()='" + PRODUCT_SAMSUNG + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        
        // Return to Home
        driver.get(BASE_URL);
        
        // Add Nokia lumia 1520
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
        clickWithRetry(By.xpath("//a[normalize-space()='" + PRODUCT_NOKIA + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        
        // Open Cart using ID
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
        
        // Wait for cart items to load properly
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody[@id='tbodyid']/tr"), 2));
        
        // Use findElements() to store cart rows in List
        List<WebElement> cartRows = driver.findElements(By.xpath("//tbody[@id='tbodyid']/tr"));
        System.out.println("Cart Row Count: " + cartRows.size());
        Assert.assertEquals(cartRows.size(), 2, "Cart should have exactly 2 items initially");
        
        // Use a loop to print product name and price
        for (WebElement row : cartRows) {
            String prodName = row.findElement(By.xpath("./td[2]")).getText();
            String prodPrice = row.findElement(By.xpath("./td[3]")).getText();
            System.out.println("Product: " + prodName + " | Price: " + prodPrice);
        }
        
        // Remove Nokia lumia 1520
        // Find the row containing Nokia and click its delete link
        WebElement nokiaDeleteLink = driver.findElement(By.xpath("//td[contains(text(), '" + PRODUCT_NOKIA + "')]/following-sibling::td/a[contains(@onclick, 'deleteItem')]"));
        nokiaDeleteLink.click();
        
        // Wait until Nokia is removed (row count becomes 1)
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody[@id='tbodyid']/tr"), 1));
        
        // Verify exactly one row remains
        cartRows = driver.findElements(By.xpath("//tbody[@id='tbodyid']/tr"));
        Assert.assertEquals(cartRows.size(), 1, "Cart should have exactly 1 item after removal");
        
        // Verify Samsung galaxy s6 remains in the cart
        String remainingProductName = cartRows.get(0).findElement(By.xpath("./td[2]")).getText();
        Assert.assertEquals(remainingProductName, PRODUCT_SAMSUNG, "Samsung galaxy s6 should remain in the cart");
        
        // Retrieve and print the cart total
        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        String cartTotal = totalElement.getText();
        System.out.println("Cart Total: " + cartTotal);
        Assert.assertFalse(cartTotal.isEmpty(), "Cart total should not be empty");
        
        
        // Capture evidence
        takeScreenshot("TC04_CartManagement");
    }

    @Test
    public void testCheckoutValidation() {
        System.out.println("--- TC05: Checkout Validation ---");
        
        
        // Start with a cart containing Samsung galaxy s6
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
        clickWithRetry(By.xpath("//a[normalize-space()='" + PRODUCT_SAMSUNG + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        
        // Open Cart
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='tbodyid']/tr")));
        
        // Click Place Order
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']"))).click();
        
        // PART A - INVALID CHECKOUT
        // Wait for modal to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderModal")));
        
        // Leave mandatory details empty and attempt to submit
        WebElement purchaseBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Purchase']")));
        purchaseBtn.click();
        
        // Handle browser validation/alert behavior
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Invalid Checkout Alert: " + alert.getText());
        Assert.assertEquals(alert.getText(), "Please fill out Name and Creditcard.", "Expected validation alert for missing details");
        
        
        alert.accept();
        
        // PART B - VALID CHECKOUT
        // Fill fields using sendKeys()
        driver.findElement(By.id("name")).sendKeys(CHECKOUT_NAME);
        driver.findElement(By.id("country")).sendKeys(CHECKOUT_COUNTRY);
        driver.findElement(By.id("city")).sendKeys(CHECKOUT_CITY);
        driver.findElement(By.id("card")).sendKeys(CHECKOUT_CARD);
        driver.findElement(By.id("month")).sendKeys(CHECKOUT_MONTH);
        driver.findElement(By.id("year")).sendKeys(CHECKOUT_YEAR);
        
        // Submit the form
        purchaseBtn.click();
        
        // Verify purchase-success message
        WebElement successMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Thank you for your purchase!')]")));
        String successMessage = successMessageElement.getText();
        System.out.println("Purchase Result: " + successMessage);
        
        // Use assertion to verify successful checkout
        Assert.assertEquals(successMessage, "Thank you for your purchase!", "Checkout success message mismatch");
        
        
        // Capture evidence
        takeScreenshot("TC05_CheckoutSuccess");
        
        // Close modal
        driver.findElement(By.xpath("//button[text()='OK']")).click();
    }
}
