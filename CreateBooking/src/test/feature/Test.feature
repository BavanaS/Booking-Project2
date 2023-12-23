Feature: API Testing Operations

  @Booking
  Scenario: Create an auth token
    When  create a new token
    Then a token should be successfully created

  Scenario: Create a new booking
    When  create a new booking
    Then a new booking should be successfully created

    Scenario: Get booking ID
      When get the booking IDs
      Then display the IDs

  Scenario: Update a booking
    Given a booking exists
    When  update the booking
    Then the booking details should be successfully updated

  Scenario: Delete a booking
    When  delete the booking
    Then the booking should be deleted