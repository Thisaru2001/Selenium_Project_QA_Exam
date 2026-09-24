# DemoBlaze Selenium Automation Project

## Project Purpose
This project is an automated smoke and regression test suite for the DemoBlaze demonstration web store. It verifies that customers can successfully open the store, browse products, add items to their cart, remove unwanted items, and complete a simple checkout validation process.

## Technologies Used
- Java 21
- Maven
- Selenium WebDriver (4.20.0)
- TestNG (7.10.1)
- Google Chrome & ChromeDriver

## Prerequisites
- Java Development Kit (JDK) installed (Version 21 recommended)
- Maven installed
- Google Chrome browser installed
- An IDE (IntelliJ IDEA or Eclipse) is recommended

## How to Open the Project
1. Open your preferred IDE (e.g., IntelliJ IDEA).
2. Select **File > Open**.
3. Navigate to the `DemoBlaze-Selenium-Automation` folder and select the `pom.xml` file.
4. Open as a Maven project and allow it to download the dependencies.

## How to Run the Tests
You can run the tests using Maven via the command line:
```bash
mvn clean test
```
Alternatively, right-click on the `DemoBlazeTests.java` file in your IDE and select **Run DemoBlazeTests**.

## Test Scenarios
1. **TC01 - Home Page Smoke Test**: Verifies the Home Page title and "PRODUCT STORE" heading.
2. **TC02 - Product Selection**: Navigates to the Phones category, selects "Samsung galaxy s6", and retrieves its price.
3. **TC03 - Add Samsung galaxy s6 to Cart**: Adds the product to the cart and handles the resulting JavaScript alert.
4. **TC04 - Cart Management**: Adds two products, removes one, and validates the remaining row, product, and cart total using loops and lists.
5. **TC05 - Checkout Validation**: Tests placing an order with invalid empty fields (handling the alert), followed by a valid checkout scenario using fictitious data.

## Test Data
All test data used is fictitious and localized within the `BaseTest.java` file as class-level constants:
- **Test URL**: `https://www.demoblaze.com/`
- **Name**: Test Student
- **Country**: Sri Lanka
- **City**: Colombo
- **Credit Card**: 4111111111111111
- **Month**: 12
- **Year**: 2027

## Important Assumptions
- The DemoBlaze web application structure remains consistent. If elements fail to load, explicit waits handle expected delays.
- As DemoBlaze is a public demo site, response times might vary. Explicit waits (WebDriverWait) have been implemented up to 10 seconds.
- Products "Samsung galaxy s6" and "Nokia lumia 1520" are available in the "Phones" category.

## Known Limitations
- The application relies on public server availability; occasional timeouts from the server could cause false-negative test failures.
- Since it's a demonstration site, state is maintained by cookies/session, so concurrent executions by other users shouldn't directly affect our test data (such as the cart), provided the browser session is isolated (handled by Selenium).
