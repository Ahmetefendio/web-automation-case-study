package com.nesine.poc.steps;

import com.nesine.poc.context.TestContext;
import com.nesine.poc.model.CouponEvent;
import com.nesine.poc.pages.BetSlip;
import com.nesine.poc.pages.ConfirmationDialog;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BasketSteps {

    private final TestContext context;
    private final BetSlip betSlip;
    private final ConfirmationDialog confirmationDialog;

    public BasketSteps(TestContext context, BetSlip betSlip, ConfirmationDialog confirmationDialog) {
        this.context = context;
        this.betSlip = betSlip;
        this.confirmationDialog = confirmationDialog;
    }

    @Then("the basket should contain coupon number {int}")
    public void basketShouldContainCoupon(int position) {
        List<CouponEvent> expected = context.getPlayedCoupon(position);
        List<CouponEvent> basket = betSlip.events();

        assertThat(basket).as("basket events").hasSameSizeAs(expected);
        for (CouponEvent event : expected) {
            assertThat(basket).as("basket should contain %s", event)
                    .anyMatch(inBasket -> CouponEvent.sameEvent(inBasket, event));
        }
    }

    @Then("a confirmation should say {string}")
    public void confirmationShouldSay(String message) {
        assertThat(confirmationDialog.message()).isEqualTo(message);
    }

    @When("the user answers {string} to the confirmation")
    public void answerConfirmation(String answer) {
        switch (answer) {
            case "Tamam" -> betSlip.confirmReplacement(confirmationDialog);
            case "Vazgeç" -> confirmationDialog.dismiss();
            default -> throw new IllegalArgumentException("Unknown answer: " + answer);
        }
    }
}
