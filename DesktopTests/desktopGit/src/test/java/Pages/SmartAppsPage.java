package Pages;

import org.openqa.selenium.By;

public class SmartAppsPage extends BasePage  {

    public static By IQFileApp = By.xpath("//h2[contains(text(),'IQ File App')]");
    public static By backButton = By.cssSelector(".x-button.x-button-back");

    public void goBack() {
        tapOnWebElement(backButton);
    }

}
