package com.nesine.poc.pages;

import com.nesine.poc.config.ConfigReader;
import com.nesine.poc.context.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    private final TestContext context;

    protected BasePage(TestContext context) {
        this.context = context;
    }

    // not cached: Pico can create a page before the @Before hook sets the driver
    protected WebDriver driver() {
        return context.getDriver();
    }

    protected WebDriverWait await() {
        return new WebDriverWait(driver(), Duration.ofSeconds(ConfigReader.getInt("timeout.explicit.seconds")));
    }

    protected void open(String path) {
        driver().get(ConfigReader.get("base.url") + path);
    }

    protected WebElement visible(By locator) {
        return await().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> allVisible(By locator) {
        return await().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // scroll to the middle, the sticky header covers elements at the top
    protected void click(By locator) {
        WebElement element = await().until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        element.click();
    }

    // cookie banner, tutorials etc.
    protected void clickIfShown(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver(), timeout).until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (TimeoutException e) {
            // not shown
        }
    }

    public void refresh() {
        driver().navigate().refresh();
    }

    public String currentUrl() {
        return driver().getCurrentUrl();
    }

    public String title() {
        return driver().getTitle();
    }
}
