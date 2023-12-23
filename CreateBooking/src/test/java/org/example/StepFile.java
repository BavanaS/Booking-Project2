package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;

public class StepFile {

    public static final String BASE_URL = "https://restful-booker.herokuapp.com";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "password123";


    @When("create a new token")
    public Response createANewToken()
    {
        JSONObject auth = new JSONObject();
        auth.put("username", USERNAME);
        auth.put("password", PASSWORD);

        Response authResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(auth.toString())
                .post("/auth");
        Assert.assertEquals(200, authResponse.statusCode());
        return authResponse;
    }

    @Then("a token should be successfully created")
    public String aTokenShouldBeSuccessfullyCreated()
    {
        Response authResponse=createANewToken();
        String token = authResponse.jsonPath().getString("token");
        return token;
    }

    @When("create a new booking")
    public Response createANewBooking()
    {
        String token=aTokenShouldBeSuccessfullyCreated();

        JSONObject booking = new JSONObject();
        booking.put("firstname", "John");
        booking.put("lastname", "Doe");
        booking.put("totalprice", 100);
        booking.put("depositpaid", true);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2023-12-01");
        bookingdates.put("checkout", "2023-12-05");

        booking.put("bookingdates", bookingdates);
        booking.put("additionalneeds", "Breakfast");

        Response bookingResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(booking.toString())
                .post("/booking");
        Assert.assertEquals(200, bookingResponse.statusCode());
        return bookingResponse;
    }


    @Then("a new booking should be successfully created")
    public int aNewBookingShouldBeSuccessfullyCreated()
    {
        Response bookingResponse=createANewBooking();
        int bookingId = bookingResponse.jsonPath().getInt("bookingid");

        return bookingId;
    }

    @When("get the booking IDs")
    public Response getTheBookingIDs()
    {
        Response bookingIdsResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .get("/booking");
        Assert.assertEquals(200, bookingIdsResponse.statusCode());

        return bookingIdsResponse;
    }

    @Then("display the IDs")
    public void displayTheIDs()
    {
        Response bookingIdsResponse=getTheBookingIDs();
        System.out.println("Booking ids: " + bookingIdsResponse.asString());
    }


    @Given("a booking exists")
    public JSONObject aBookingExists()
    {
        JSONObject updatedBooking = new JSONObject();
        updatedBooking.put("firstname", "John");
        updatedBooking.put("lastname", "Doe");
        updatedBooking.put("totalprice", 150);
        updatedBooking.put("depositpaid", false);

        JSONObject updatedBookingdates = new JSONObject();
        updatedBookingdates.put("checkin", "2023-12-10");
        updatedBookingdates.put("checkout", "2023-12-15");

        updatedBooking.put("bookingdates", updatedBookingdates);
        updatedBooking.put("additionalneeds", "Lunch");

        return updatedBooking;
    }

    @When("update the booking")
    public Response updateTheBooking()
    {
        String token=aTokenShouldBeSuccessfullyCreated();
        int bookingId=aNewBookingShouldBeSuccessfullyCreated();
        JSONObject updatedBooking=aBookingExists();

        Response updatedBookingResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(updatedBooking.toString())
                .put("/booking/" + bookingId);
        Assert.assertEquals(200, updatedBookingResponse.statusCode());

        return updatedBookingResponse;
    }

    @Then("the booking details should be successfully updated")
    public void theBookingDetailsShouldBeSuccessfullyUpdated()
    {
        Response updatedBookingResponse=updateTheBooking();
        System.out.println("Updated booking: " + updatedBookingResponse.asString());
    }

    @When("delete the booking")
    public Response deleteTheBooking()
    {
        int bookingId=aNewBookingShouldBeSuccessfullyCreated();
        String token=aTokenShouldBeSuccessfullyCreated();

        Response deleteBookingResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Cookie", "token=" + token)
                .delete("/booking/" + bookingId);
        return deleteBookingResponse;
    }

    @Then("the booking should be deleted")
    public void theBookingShouldBeDeleted()
    {
        Response deleteBookingResponse=deleteTheBooking();
        System.out.println("Delete booking status code: " + deleteBookingResponse.statusCode());
    }
}
