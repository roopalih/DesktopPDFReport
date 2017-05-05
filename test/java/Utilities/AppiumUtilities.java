package Utilities;

//
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.List;
import java.util.Set;

public class AppiumUtilities {

    protected static AppiumDriver driver;

    public AppiumUtilities() {}

    public void waitForVisibilityOf(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForVisibilityOf(By locator, long seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToBeRemoved(By locator) {
        waitForElementToBeRemoved(locator, 30);
    }

    public void waitForElementToBeRemoved(By locator, long seconds){

        ExpectedCondition<Boolean> doesNotExist = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List elements = driver.findElements(locator);
                return elements.size() == 0;
            }
        };

        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(doesNotExist);
    }

    public void waitForClickabilityOf(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForClickabilityOf(By locator, long seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForDocumentReady() {
        waitForDocumentReady(30);
    }

    public void waitForDocumentReady(long seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);

        ExpectedCondition<Boolean> documentReady = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        wait.until(documentReady);
    }

    public boolean takeScreenshot(final String name) {
        String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
        //String screenshotDirectory = System.getProperty("appium.screenshots.dir", "C:\\appiumScreenshots");
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return screenshot.renameTo(new File(screenshotDirectory, String.format("%s.png", name)));
    }

    public void switchToWebView() {

        // Wait for WebView before switching context
        if (driver.getContext().equals("NATIVE_APP")) {
            waitForVisibilityOf(By.className("android.webkit.WebView"));
        }

        Set<String> contextNames = driver.getContextHandles();

        for (String context : contextNames) {
            if (context.contains("WEBVIEW")) {
                driver.context(context.toString());
                break;
            }
        }
    }

    public void switchToNativeView() {
        driver.context("NATIVE_APP");
    }


    // this method taps on element in web context
    // based off of https://github.com/appium/appium/issues/2816
    public void tapOnWebElement(By locator) {

        WebElement element = driver.findElement(locator);

        // Fetch DOM document full size
        double documentWidth = Double.parseDouble(driver.executeScript("return document.documentElement.scrollWidth").toString());
        double documentHeight = Double.parseDouble(driver.executeScript("return document.documentElement.scrollHeight").toString());

        // Fetch element center position from the DOM
        double elementLeftCenter = element.getLocation().getX() + (element.getSize().getWidth() / 2);
        double elementTopCenter = element.getLocation().getY() + (element.getSize().getHeight() / 2);

        switchToNativeView();

        // Fetch the cordova WebView element
        WebElement androidWebView = driver.findElement(By.className("android.webkit.WebView"));

        double screenXMultiplier = androidWebView.getSize().getWidth() / documentWidth;
        double screenYMultiplier = androidWebView.getSize().getHeight() / documentHeight;

        int tapX = (int)(elementLeftCenter * screenXMultiplier);
        int tapY = (int)(elementTopCenter * screenYMultiplier) + androidWebView.getLocation().getY();

        // Send a tap event on the specific button position
        driver.tap(1, tapX, tapY, 200);

        switchToWebView();
    }

    public boolean isAlertPresent() {

        boolean presentFlag = false;

        try {

            // Check the presence of alert
            Alert alert = driver.switchTo().alert();
            // Alert present; set the flag
            presentFlag = true;
            // if present consume the alert
            alert.accept();
            //( Now, click on ok or cancel button )

        } catch (NoAlertPresentException ex) {
            // Alert not present
        }

        return presentFlag;
    }
}
