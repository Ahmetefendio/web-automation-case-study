@coupon
Feature: Popular coupons
  A coupon played from Popular Coupons stays in the basket while browsing the header pages

  Scenario Outline: Popular coupon #<position> stays in the basket across header pages
    Given the user is on the home page
    When the user opens Popular Coupons
    And the user plays popular coupon number <position>
    Then the basket should contain coupon number <position>

    When the user clicks the header link to "/iddaa"
    Then the "/iddaa" page should be open with a title containing "İddaa Programı"
    And the basket should contain coupon number <position>

    When the user clicks the header link to "/iddaa/canli-skor/futbol"
    Then the "/iddaa/canli-skor/futbol" page should be open with a title containing "Canlı Skor"
    And the basket should contain coupon number <position>

    When the user clicks the header link to "/iddaa/dakika-bahis"
    Then the "/iddaa/dakika-bahis" page should be open with a title containing "Dakika Bahis"
    And the basket should contain coupon number <position>

    Examples:
      | position |
      | 1        |
      | 2        |
      | 3        |
