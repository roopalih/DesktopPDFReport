package Tests;

import Pages.LandingPage;
import Pages.LoginPage;
import Pages.ZoneCentralPage;
import Utilities.AppiumUtilities;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public abstract class TestBase extends AppiumUtilities {
    public WebDriver driver;

    //Before suite method runs at the start of the test suite
    @BeforeSuite
    public void setUpAppium() throws MalformedURLException {
        // We SHOULD NOT set our own DesiredCapabilities - Device Farm does this at the server level
        driver =  new WebDriver();

        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);

        loginToZone(); //Login method called
    }

    //This is method for login to a zone
    private void loginToZone() {
        // because we are testing a hybrid app
        switchToWebView(); // switching to webview

        // go to production sax
        driver.get("https://www.smartapp.com/gid/mobile/gettoken?ctp=%7b'ht'%3a'drd'%2c'ac'%3a'zc'%2c'lo'%3a'no'%2c'ver'%3a'AppV15'%2c'ct'%3a'hil'%7d");

        new LoginPage().login();
        new ZoneCentralPage().goToZone();
        new LandingPage().waitForPageToLoad();
    }

    // Runs at the last
    @AfterSuite
    public void tearDownAppium() {
        driver.quit();
    }

    @AfterClass
    public void restartApp() {
        //switchToNativeView();
        //driver.resetApp();

        // This refreshes in browser to go back to landing page
    	
    	//There are some errors in the next line
        ((Object) driver).executeScript("window.location.reload();");
    }

    @AfterMethod
    public void takeScreenShotOfFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            switchToNativeView();
            takeScreenshot("testFailure");
            switchToWebView();
        }
    }
}

