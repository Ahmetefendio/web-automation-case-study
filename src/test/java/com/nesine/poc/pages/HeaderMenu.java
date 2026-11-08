package com.nesine.poc.pages;

import com.nesine.poc.context.TestContext;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class HeaderMenu extends BasePage {

    // "Değerli Bahis Nedir?" tutorial, shown on the first visit of /iddaa and covers the basket
    private static final By SKIP_TUTORIAL = By.xpath("//div[@id='tutorial']//button[contains(normalize-space(),'istemiyorum')]");

    public HeaderMenu(TestContext context) {
        super(context);
    }

    // Found by href, not by text: the item labelled "İddaa" goes to "/", the one that opens /iddaa is "Bülten"
    @Step("Click header link to {path}")
    public void clickLinkTo(String path) {
        click(By.cssSelector("ul[data-test-id='MainCategories'] a[href='" + path + "']"));
        await().until(ExpectedConditions.urlContains(path));
        clickIfShown(SKIP_TUTORIAL, Duration.ofSeconds(3));
    }
}
