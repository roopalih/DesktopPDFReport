package Tests;

import Pages.LandingPage;
import Pages.LoginPage;
import Pages.ZoneCentralPage;
import Utilities.AppiumUtilities;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public abstract class TestBase extends AppiumUtilities {

    @BeforeSuite
    public void setUpAppium() throws MalformedURLException {
        // We SHOULD NOT set our own DesiredCapabilities - Device Farm does this at the server level
        driver =  new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), new DesiredCapabilities());

        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);

        loginToZone();
    }

    private void loginToZone() {
        // because we are testing a hybrid app
        switchToWebView();

        // go to production sax
        driver.get("https://www.smartapp.com/gid/mobile/gettoken?ctp=%7b'ht'%3a'drd'%2c'ac'%3a'zc'%2c'lo'%3a'no'%2c'ver'%3a'AppV15'%2c'ct'%3a'hil'%7d");

        new LoginPage().login();
        new ZoneCentralPage().goToZone();
        new LandingPage().waitForPageToLoad();
    }

    @AfterSuite
    public void tearDownAppium() {
        driver.quit();
    }

    @AfterClass
    public void restartApp() {
        //switchToNativeView();
        //driver.resetApp();

        // This refreshes in browser to go back to landing page
        driver.executeScript("window.location.reload();");
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

