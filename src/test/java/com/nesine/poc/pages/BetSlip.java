package com.nesine.poc.pages;

import com.nesine.poc.context.TestContext;
import com.nesine.poc.model.CouponEvent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** The "İddaa Kuponum" basket on the right side of every page. */
public class BetSlip extends BasePage {

    private static final By BASKET = By.id("basketCoupon");
    private static final By MINIMIZED_BASKET = By.id("coupon-info");
    private static final By ROWS = By.cssSelector("#basketCoupon .couponRow");

    // Canlı Sonuçlar has ~20k DOM nodes and every WebDriver call takes 0.5s+ there,
    // reading the rows one by one took 30-60s, so they are read in one script
    private static final String READ_ROWS = """
            return [...document.querySelectorAll('#basketCoupon .couponRow')]
                .map(row => [row.querySelector('.eventName').innerText, row.querySelector('.eventDate').innerText]);
            """;

    public BetSlip(TestContext context) {
        super(context);
    }

    // Canlı Sonuçlar and Dakika Bahis only show the minimized basket at the bottom right
    @Step("Open basket")
    public void open() {
        try {
            new WebDriverWait(driver(), Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(BASKET));
        } catch (TimeoutException e) {
            click(MINIMIZED_BASKET);
            visible(BASKET);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CouponEvent> events() {
        open();
        visible(ROWS);
        List<List<String>> rows = (List<List<String>>) ((JavascriptExecutor) driver()).executeScript(READ_ROWS);
        return rows.stream().map(row -> CouponEvent.of(row.get(0), row.get(1))).toList();
    }

    // the rows are re-rendered ~0.7s later, wait so the next read doesn't see the old coupon
    @Step("Confirm replacing the basket")
    public void confirmReplacement(ConfirmationDialog dialog) {
        open();
        WebElement oldRow = visible(ROWS);
        dialog.accept();
        await().until(ExpectedConditions.stalenessOf(oldRow));
    }
}
