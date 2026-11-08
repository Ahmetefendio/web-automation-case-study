package com.nesine.poc.steps;

import com.nesine.poc.context.TestContext;
import com.nesine.poc.model.CouponEvent;
import com.nesine.poc.pages.HomePage;
import com.nesine.poc.pages.PopularCouponsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponSteps {

    private final TestContext context;
    private final HomePage homePage;
    private final PopularCouponsPage popularCouponsPage;

    public CouponSteps(TestContext context, HomePage homePage, PopularCouponsPage popularCouponsPage) {
        this.context = context;
        this.homePage = homePage;
        this.popularCouponsPage = popularCouponsPage;
    }

    @Given("the user is on the home page")
    public void theUserIsOnTheHomePage() {
        homePage.openPage();
    }

    @When("the user opens Popular Coupons")
    public void theUserOpensPopularCoupons() {
        homePage.openPopularCoupons();
    }

    @When("the user plays popular coupon number {int}")
    public void theUserPlaysPopularCoupon(int position) {
        List<CouponEvent> events = popularCouponsPage.eventsOfCoupon(position);
        assertThat(events).as("events of coupon #%d", position).isNotEmpty();
        context.addPlayedCoupon(position, events);
        popularCouponsPage.playCoupon(position);
    }
}
