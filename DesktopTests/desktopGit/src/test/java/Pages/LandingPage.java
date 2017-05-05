package Pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LandingPage extends BasePage {
    public static WebDriver driver;

    By zoneCentral_Button = By.cssSelector("div.x-button-normal.x-button.ZoneCentral.zoneCenProjectBTNModified");
    By homeButton = By.cssSelector(".x-button-icon.home-picto.x-icon-mask");
    public static By smartAppsButton = By.cssSelector("#smartapps");

    By initLoad = By.cssSelector("#initload"); // This is the "Syncing AppZone Cache" screen

    // This can take a long time the first time because has to download all content onto mobile device
    public LandingPage waitForPageToLoad() {

        try {

            isAlertPresent();

            waitForVisibilityOf(smartAppsButton, 300); // wait up to 5 minutes
            waitForElementToBeRemoved(initLoad, 600); // 10 minute timeout

            switchToNativeView();
            takeScreenshot("landingPage");
            switchToWebView();
        }
        catch (UnhandledAlertException e) {

            WebDriverWait wait = new WebDriverWait(driver, 5);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            switchToNativeView();
            takeScreenshot("landingPageAlert");
            switchToWebView();

            alert.accept();
        }

        return new LandingPage();
    }

    public static boolean at() {

        String currentURL = driver.getCurrentUrl();

        String baseUrl = "https://ioscert.smartapp.com";

        if (!(currentURL.startsWith(baseUrl) || currentURL.startsWith("file://"))) {
            return false;
        }

        if (driver.findElements(smartAppsButton).isEmpty()) {
            return false;
        }

        return true;
    }
}
