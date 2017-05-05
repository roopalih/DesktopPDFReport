package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ZoneCentralPage extends BasePage  {
    public WebDriver driver;

    By logout_Button = By.cssSelector("#logoutBtn");
    By iosCert = By.cssSelector("div[zoneid=\"13274\"]");

    public ZoneCentralPage goToZone() {

        try {
            isAlertPresent();

            waitForVisibilityOf(iosCert, 60);

            switchToNativeView();
            takeScreenshot("zoneCentral");
            switchToWebView();

            driver.findElement(iosCert).click();
        }
        catch (NoSuchElementException | TimeoutException e) {
            // Element might not become visible because might automatically go to zone if there is only one possible appzone or if we were already logged in
        }
        catch (UnhandledAlertException e) {

            WebDriverWait wait = new WebDriverWait(driver, 5);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            switchToNativeView();
            takeScreenshot("ZoneCentralPageAlert");
            switchToWebView();

            alert.accept();
        }

        return new ZoneCentralPage();
    }
}
