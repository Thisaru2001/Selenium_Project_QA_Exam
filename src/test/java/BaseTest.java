import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class BaseTest {

    // Suppress Selenium/CDP/SLF4J console warnings
    static {
        System.setProperty("webdriver.chrome.silentOutput", "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.openqa.selenium.devtools").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.openqa.selenium.chromium").setLevel(java.util.logging.Level.OFF);
    }

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Reusable Data (Class-level constants)
    protected static final String BASE_URL = "https://www.demoblaze.com/";
    protected static final String PRODUCT_SAMSUNG = "Samsung galaxy s6";
    protected static final String PRODUCT_NOKIA = "Nokia lumia 1520";
    
    protected static final String CHECKOUT_NAME = "Test Student";
    protected static final String CHECKOUT_COUNTRY = "Sri Lanka";
    protected static final String CHECKOUT_CITY = "Colombo";
    protected static final String CHECKOUT_CARD = "4111111111111111";
    protected static final String CHECKOUT_MONTH = "12";
    protected static final String CHECKOUT_YEAR = "2027";

    @BeforeMethod
    public void setUp() {
        // Setup Chrome driver with options to suppress browser-level warnings
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--log-level=3");      // Suppress CDP/DevTools warnings
        options.addArguments("--silent");            // Suppress browser console output
        driver = new ChromeDriver(options);
        
        // Initialize explicit wait for 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Maximize browser window
        driver.manage().window().maximize();
        
        // Navigate to the base URL
        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // Safe cleanup of browser session
        if (driver != null) {
            driver.quit();
        }
    }


    protected void takeScreenshot(String fileName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File("screenshots/" + fileName + ".png");
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + destFile.getPath());
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}

