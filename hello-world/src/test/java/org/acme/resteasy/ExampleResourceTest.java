package org.acme.resteasy;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/helloworld")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

    @Test
    public void testHelloQueryParam() {
        given()
                .pathParam("name", "fabricio")
                .when().get("/helloworld/{name}")
                .then()
                .statusCode(200)
                .body(is("hello fabricio"));
    }

}