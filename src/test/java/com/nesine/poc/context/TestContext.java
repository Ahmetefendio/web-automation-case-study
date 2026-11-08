package com.nesine.poc.context;

import com.nesine.poc.model.CouponEvent;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Per-scenario state. Pico creates a new instance for every scenario, so parallel
 * scenarios never share it.
 */
public class TestContext {

    private WebDriver driver;
    private final Map<Integer, List<CouponEvent>> playedCoupons = new HashMap<>();

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void addPlayedCoupon(int position, List<CouponEvent> events) {
        playedCoupons.put(position, List.copyOf(events));
    }

    public List<CouponEvent> getPlayedCoupon(int position) {
        List<CouponEvent> events = playedCoupons.get(position);
        if (events == null) {
            throw new IllegalStateException("Coupon #" + position + " was not played in this scenario");
        }
        return events;
    }
}
