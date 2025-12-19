# SauceDemo Web Automation Testing Framework

## 👋 Overview
This repository contains an **automation testing framework** for the SauceDemo e-commerce web application.
The framework is implemented using **Java**, **Selenium WebDriver**, and **TestNG**, with **Allure Reports** for detailed test execution reporting.

The project is designed to be **modular, maintainable, and scalable**, following clean automation practices and structured framework design suitable for real-world automation projects.

---

## 🎯 Objectives
- Automate core user workflows of the SauceDemo web application
- Validate functional behavior across critical features
- Support structured execution (Smoke, Regression, End-to-End)
- Provide clear and reliable test reporting using Allure

---

## ✅ Test Coverage
The automation suite covers the following areas:
- Login functionality (valid & invalid scenarios)
- Inventory and product details
- Cart operations and item management
- Checkout flow (step-by-step validation)
- End-to-End purchase scenarios
- Session handling and logout
- Basic access-control and security-related checks

Tests are organized by feature for clarity and maintainability.

---

## 🧰 Tech Stack
- **Language:** Java
- **UI Automation:** Selenium WebDriver
- **Test Framework:** TestNG
- **Build Tool:** Maven
- **Reporting:** Allure Reports
- **Logging:** Log4j2

---

## 🏗️ Framework Architecture
The framework follows a layered and modular design:

- **Page Object Model (POM)** for UI abstraction
- **Base test classes** to centralize setup, teardown, and shared flows
- **Reusable components** for common UI elements
- **Custom utilities** for:
    - WebDriver management
    - Wait handling and synchronization
    - Element interactions
    - Assertions and soft assertions
    - Logging and reporting
- **Data-driven testing** using JSON files
- **Test grouping** using TestNG (Smoke, Regression, E2E)

---

## ⭐ Project Highlights
- Clean Page Object Model (POM) implementation
- JSON-based data-driven testing
- Custom assertion and soft-assert handling
- Modular utilities for driver management, waits, and logging
- Structured TestNG execution (Smoke, Regression, E2E)

---

## 📁 Project Structure (High Level)
- `src/main/java` → Core framework (pages, components, utilities, drivers, listeners)
- `src/test/java` → Test suites, base tests, data providers, and models
- `src/test/resources/testData` → JSON-based test data
- `TestRunners/` → TestNG XML suites (smoke, regression, full suite)

---

## ▶️ How to Run

### Prerequisites
- Java installed (recommended: Java 17)
- Maven installed
- Chrome browser installed
- Allure installed (for report generation)

### Steps
***1. Clone the repository***
   ```
git clone https://github.com/MohamedKamal-eng/saucedemo-automation-testing.git
cd saucedemo-automation-testing
   ```
***2. Run all tests***
   ```
mvn clean test
   ```

***3. Run specific test suites***
  ```
mvn clean test -DsuiteXmlFile=TestRunners/smoke.xml
mvn clean test -DsuiteXmlFile=TestRunners/regression.xml
mvn clean test -DsuiteXmlFile=TestRunners/fullSuite.xml
  ```
---

## 📊 Test Reports
- **Allure Reports** are generated after test execution
- Reports provide:
    - Test execution status
    - Step-by-step test details
    - Screenshots on failure
    - Detailed assertion results

To generate and view the Allure report:
 ```bash
   allure serve 
```
---

## 🚀 Future Enhancements
- Integrate Continuous Integration pipelines (GitHub Actions / Jenkins)
- Add cross-browser execution support
- Enhance reporting with environment and execution metadata
- Expand negative and security test coverage

---

## 👤 Author
**Mohamed Kamal**  
Software Test Engineer | Manual Testing | Test Automation | Selenium | Java | TestNG | Allure
