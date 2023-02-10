import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Settings {

    protected AndroidDriver driver;
    String AppiumServer = "127.0.0.1";
    String AppiumPort = "4723";
    String deviceName = "emulator-5554";
    String androidVersion = "9";

    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", androidVersion);
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");

        File file = new File("src/main/resources", "org.wikipedia.apk");
        capabilities.setCapability("app", file.getAbsolutePath());

        driver = new AndroidDriver(new URL("http://" + AppiumServer + ":" + AppiumPort + "/wd/hub"), capabilities);
    }

    @Test
    public void testStart(){
        clickElement(By.id("org.wikipedia:id/search_container"));
        fillText(By.id("org.wikipedia:id/search_src_text"), "Java");
        hideKeyboard();
        assertCountElementsOnPage(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title']"));
        clearSearch();
        assertSearchResultIsMissing(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title']"));
    }

    public void setWait(By elementBy){
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementBy));
    }
    public void clickElement(By elementBy){
        setWait(elementBy);
        driver.findElement(elementBy).click();
    }
    public void hideKeyboard(){
        driver.hideKeyboard();
    }

    public void fillText(By elementBy, String text){
        setWait(elementBy);
        driver.findElement(elementBy).sendKeys(text);
    }
    public void assertCountElementsOnPage(By elementBy){
        setWait(elementBy);
        List elements = driver.findElements(elementBy);
        Assertions.assertEquals(8,elements.stream().count(), "The number of articles does not match ");
    }
    public void clearSearch(){
        clickElement(By.id("org.wikipedia:id/search_close_btn"));
    }
    public void assertSearchResultIsMissing(By elementBy){
        List elements = driver.findElements(elementBy);
        Assertions.assertTrue(elements.isEmpty(), "is not Empty");
    }

}
