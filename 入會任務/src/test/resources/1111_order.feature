@double_eleven_promotion
Feature: Double Eleven Promotion
  As a shopper
  I want the system to apply double eleven promotion discounts
  So that I can get 20% off when buying 10 or more of the same product

  Scenario: Double eleven promotion - exactly 10 items of same product
    Given the double eleven promotion is active
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 10       | 100       |
    Then the order summary should be:
      | totalAmount |
      | 800         |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 10       |

  Scenario: Double eleven promotion - 12 items of same product
    Given the double eleven promotion is active
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: Double eleven promotion - 27 items of same product
    Given the double eleven promotion is active
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 27       | 100       |
    Then the order summary should be:
      | totalAmount |
      | 2300        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 27       |

  Scenario: Double eleven promotion - 10 different products (no discount)
    Given the double eleven promotion is active
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 商品A        | 1        | 100       |
      | 商品B        | 1        | 100       |
      | 商品C        | 1        | 100       |
      | 商品D        | 1        | 100       |
      | 商品E        | 1        | 100       |
      | 商品F        | 1        | 100       |
      | 商品G        | 1        | 100       |
      | 商品H        | 1        | 100       |
      | 商品I        | 1        | 100       |
      | 商品J        | 1        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 商品A        | 1        |
      | 商品B        | 1        |
      | 商品C        | 1        |
      | 商品D        | 1        |
      | 商品E        | 1        |
      | 商品F        | 1        |
      | 商品G        | 1        |
      | 商品H        | 1        |
      | 商品I        | 1        |
      | 商品J        | 1        |

  Scenario: Double eleven promotion - mixed quantities
    Given the double eleven promotion is active
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 15       | 100       |
      | 帽子          | 5        | 200       |
      | 手套          | 20       | 50        |
    Then the order summary should be:
      | totalAmount |
      | 3100        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 15       |
      | 帽子          | 5        |
      | 手套          | 20       |

  Scenario: Double eleven promotion - multiple sets of 10
    Given the double eleven promotion is active
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 25       | 100       |
    Then the order summary should be:
      | totalAmount |
      | 2100        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 25       |

  Scenario: Double eleven promotion combined with threshold discount
    Given the double eleven promotion is active
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 2000      | 200      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 20       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1600           | 0        | 1600        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 20       |

  Scenario: Double eleven promotion combined with threshold discount - meets threshold
    Given the double eleven promotion is active
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 1500      | 200      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 20       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1600           | 200      | 1400        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 20       |

  Scenario: Three promotions stacked - buy one get one + double eleven + threshold discount
    Given the double eleven promotion is active
    And the buy one get one promotion for cosmetics is active
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 2000      | 300      |
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | 口紅          | cosmetics | 15       | 200       |
      | 襪子          | apparel   | 20       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 4400           | 300      | 4100        |
    And the customer should receive:
      | productName | quantity |
      | 口紅          | 16       |
      | 襪子          | 20       |

  Scenario: Double eleven promotion - boundary test (exactly at threshold)
    Given the double eleven promotion is active
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 1600      | 200      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 20       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1600           | 200      | 1400        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 20       |

  Scenario: Double eleven promotion - just below threshold
    Given the double eleven promotion is active
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 1600      | 200      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 19       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1700           | 200      | 1500        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 19       |

  Scenario: Double eleven promotion with different threshold amounts
    Given the double eleven promotion is active
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 1000      | 100      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 15       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1300           | 100      | 1200        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 15       |

  Scenario: Double eleven promotion - multiple products with different discount groups
    Given the double eleven promotion is active
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 3000      | 500      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 25       | 100       |
      | 帽子          | 15       | 200       |
      | 手套          | 5        | 50        |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 4950           | 500      | 4450        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 25       |
      | 帽子          | 15       |
      | 手套          | 5        |
