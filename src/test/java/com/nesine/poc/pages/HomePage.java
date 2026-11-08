package com.nesine.poc.pages;

import com.nesine.poc.context.TestContext;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;

public class HomePage extends BasePage {

    private static final By REJECT_COOKIES = By.xpath("//button[normalize-space()='Reddet']");
    private static final By POPULAR_COUPONS =
            By.cssSelector("ul[data-test-id='MainCategories'] a[href='/iddaa/populer-kuponlar']");

    public HomePage(TestContext context) {
        super(context);
    }

    @Step("Open home page")
    public void openPage() {
        open("/");
        clickIfShown(REJECT_COOKIES, Duration.ofSeconds(5));
    }

    @Step("Click 'Popüler Kuponlar'")
    public void openPopularCoupons() {
        click(POPULAR_COUPONS);
    }
}
