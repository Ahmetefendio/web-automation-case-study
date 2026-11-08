@basket-replace
Feature: Playing a coupon when the basket is not empty

  Scenario Outline: Second coupon, answer "<answer>"
    Given the user is on the home page
    And the user opens Popular Coupons
    And the user plays popular coupon number 1
    And the basket should contain coupon number 1

    When the user plays popular coupon number 2
    Then a confirmation should say "Kuponunuzdaki maçlar silinecek. Emin misiniz?"

    When the user answers "<answer>" to the confirmation
    Then the basket should contain coupon number <kept>

    When the user refreshes the page
    Then the basket should contain coupon number <kept>

    Examples:
      | answer | kept |
      | Tamam  | 2    |
      | Vazgeç | 1    |
