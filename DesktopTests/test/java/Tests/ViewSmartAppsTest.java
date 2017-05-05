package Tests;

import Pages.LandingPage;
import Pages.LoginPage;
import Pages.SmartAppsPage;
import Pages.ZoneCentralPage;
import org.testng.annotations.Test;

public class ViewSmartAppsTest extends TestBase {

    @Test
    public void goToSmartAppsList() throws InterruptedException {

        // navigate to Smart Apps List
        driver.findElement(LandingPage.smartAppsButton).click();

        // wait for page to load
        waitForVisibilityOf(SmartAppsPage.IQFileApp);

        switchToNativeView();
        takeScreenshot("smartApps");
        switchToWebView();
    }
}
