Feature: Just a sample
  Even a sample likes a short description.
  The more lines the better.

  Background: Shared between all
    Given alpha

  Scenario: Samples are good
  A scenario deserves a description as well.
  Even multiple lines
    Given A
    When B
    Then C

  Scenario Outline: Parametrized samples are even better
  Again, a short description.
  In multiple lines.
    Given <parameter>
    When <action>
    Then <result>
    Examples:
      | parameter | action | result |
      | person    | jump   | up     |
      | rabbit    | duck   | hidden |
