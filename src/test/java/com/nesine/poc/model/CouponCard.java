package com.nesine.poc.model;

/** A card on the Popular Coupons list. matchCount is the number in the header ("3 Maç"). */
public record CouponCard(int position, int matchCount, int eventCount) {
}
