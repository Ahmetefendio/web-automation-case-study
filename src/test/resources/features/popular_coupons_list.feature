@filter
Feature: Popular coupons filters

  Scenario: Every filter lists only coupons with the expected match count
    Given the user is on the home page
    When the user opens Popular Coupons
    Then every filter in the test data should list only matching coupons
