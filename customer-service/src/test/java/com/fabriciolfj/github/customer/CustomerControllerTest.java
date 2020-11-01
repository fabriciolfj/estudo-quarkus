package com.fabriciolfj.github.customer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.json.Json;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

//@QuarkusTest
public class CustomerControllerTest {

    //@Test
    public void testCustomerService() {

        var obj = Json.createObjectBuilder()
                .add("name", "fabricio")
                .build();

        given().contentType("application/json")
                .body(obj)
                .when()
                .post("/customers")
                .then()
                .statusCode(201);
    }
}