Feature: Introduce addition2

Scenario: Should add two numbers
Given A
And B
When added together
Then the result will be addition of both numbers

Scenario: Should add two numbers again
Given A
And C
When added together
And subtracted by C
Then the result will be A
But C will be C
And A will be A