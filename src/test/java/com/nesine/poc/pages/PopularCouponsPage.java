package com.nesine.poc.pages;

import com.nesine.poc.context.TestContext;
import com.nesine.poc.model.CouponCard;
import com.nesine.poc.model.CouponEvent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class PopularCouponsPage extends BasePage {

    // the "Hemen Oyna" tab at the top is a button too, coupon buttons are the nsn-button ones
    private static final String PLAY_BUTTONS = "//button[@data-testid='nsn-button'][normalize-space()='Hemen Oyna']";
    private static final By COUPON_CARDS = By.xpath(PLAY_BUTTONS + "/ancestor::div[@data-cs][1]");
    private static final By EVENT_ROW = By.cssSelector("[data-testid^='event-row-']");
    private static final By EVENT_NAME = By.cssSelector("[data-testid='event-name']");
    // no test id on the date or the header, see README
    private static final By EVENT_DATE = By.xpath(".//span[@data-testid='event-rate']/../following-sibling::div[1]");
    private static final By MATCH_COUNT = By.xpath(".//span[normalize-space()='Maç']/preceding-sibling::b[1]");

    public PopularCouponsPage(TestContext context) {
        super(context);
    }

    private static By playButton(int position) {
        return By.xpath("(" + PLAY_BUTTONS + ")[" + position + "]");
    }

    private static By couponCard(int position) {
        return By.xpath("(" + PLAY_BUTTONS + ")[" + position + "]/ancestor::div[@data-cs][1]");
    }

    // no test id at 1920px width (only on narrow layouts)
    private static By filterButton(String name) {
        return By.xpath("//button[normalize-space()='" + name + "']");
    }

    public List<CouponEvent> eventsOfCoupon(int position) {
        WebElement card = visible(couponCard(position));
        return card.findElements(EVENT_ROW).stream()
                .map(row -> CouponEvent.of(row.findElement(EVENT_NAME).getText(), row.findElement(EVENT_DATE).getText()))
                .toList();
    }

    @Step("Click 'Hemen Oyna' on coupon #{position}")
    public void playCoupon(int position) {
        click(playButton(position));
    }

    // the list is re-rendered on every filter click
    @Step("Select the '{name}' filter")
    public void selectFilter(String name) {
        WebElement firstCardBefore = visible(COUPON_CARDS);
        click(filterButton(name));
        await().until(ExpectedConditions.stalenessOf(firstCardBefore));
    }

    public List<CouponCard> couponCards() {
        List<WebElement> cards = allVisible(COUPON_CARDS);
        List<CouponCard> result = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            WebElement card = cards.get(i);
            result.add(new CouponCard(
                    i + 1,
                    Integer.parseInt(card.findElement(MATCH_COUNT).getText().trim()),
                    card.findElements(EVENT_ROW).size()));
        }
        return result;
    }
}
