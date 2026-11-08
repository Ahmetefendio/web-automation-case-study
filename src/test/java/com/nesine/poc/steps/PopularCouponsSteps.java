package com.nesine.poc.steps;

import com.nesine.poc.config.TestData;
import com.nesine.poc.model.CouponCard;
import com.nesine.poc.model.CouponFilter;
import com.nesine.poc.pages.PopularCouponsPage;
import io.cucumber.java.en.Then;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PopularCouponsSteps {

    private final PopularCouponsPage popularCouponsPage;

    public PopularCouponsSteps(PopularCouponsPage popularCouponsPage) {
        this.popularCouponsPage = popularCouponsPage;
    }

    // soft assertions: a wrong filter should not hide the result of the others
    @Then("every filter in the test data should list only matching coupons")
    public void everyFilterShouldListMatchingCoupons() {
        List<CouponFilter> filters = TestData.couponFilters();
        assertThat(filters).as("filters in the test data").isNotEmpty();

        SoftAssertions softly = new SoftAssertions();
        for (CouponFilter filter : filters) {
            popularCouponsPage.selectFilter(filter.name());
            List<CouponCard> cards = popularCouponsPage.couponCards();
            softly.assertThat(cards).as("coupons of '%s'", filter.name()).isNotEmpty();
            for (CouponCard card : cards) {
                softly.assertThat(card.eventCount())
                        .as("'%s' coupon #%d events", filter.name(), card.position())
                        .isBetween(filter.minEvents(), filter.maxEvents());
                softly.assertThat(card.matchCount())
                        .as("'%s' coupon #%d match count", filter.name(), card.position())
                        .isEqualTo(card.eventCount());
            }
        }
        softly.assertAll();
    }
}
