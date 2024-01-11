package com.kineteco;

import com.kineteco.model.ProductInventory;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductsInventoryResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/products")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy is up"));
    }

    @Test
    public void testInventory() {
        given()
                .when().get("/products/123")
                .then()
                .statusCode(200)
                .extract().body().as(ProductInventory.class);
    }
}