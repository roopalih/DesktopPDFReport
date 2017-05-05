package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    By userName = By.cssSelector("#loginUsername");
    By password = By.cssSelector("#loginPassword");
    By login_Button = By.cssSelector("#LoginBtn");

    public WebDriver driver;
    public LoginPage login() {

        try {

            isAlertPresent();

            waitForVisibilityOf(userName);

            switchToNativeView();
            takeScreenshot("loginPage");
            switchToWebView();

            driver.findElement(userName).sendKeys("inquestios@gmail.com");
            driver.findElement(password).sendKeys("password");

            switchToNativeView();
            takeScreenshot("loginPage2");
            switchToWebView();

            try {
                //driver.hideKeyboard();
            }
            catch (WebDriverException e)
            {
                // On AWS DeviceFarm, hideKeyboard is throwing exception. hideKeyboard is needed when running locally because the keyboard is covering the login_Button after filling out form
                // org.openqa.selenium.WebDriverException: An unknown server-side error occurred while processing the command. (Original error: Soft keyboard not present, cannot hide keyboard)
            }

            tapOnWebElement(login_Button);
        }
        catch (NoSuchElementException | TimeoutException e) {
            // Element might not become visible because might automatically go to zone if there is only one possible appzone or if we were already logged in
        }
        catch (UnhandledAlertException e) {

            WebDriverWait wait = new WebDriverWait(driver, 5);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            switchToNativeView();
            takeScreenshot("LoginPageAlert");
            switchToWebView();

            alert.accept();
        }

        return new LoginPage();
    }
}
