package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class SagaResourceTest {

    @Test
    void testPurchaseEndpoint() {
        Map<String, Object> purchase = new HashMap<>();
        purchase.put("amount", 70);
        given()
                .contentType(ContentType.JSON)
                .body(purchase)
                .when().post("/saga/purchase")
                .then()
                .statusCode(200);
    }

    @Test
    void testBalanceEndpoint() {
        given()
                .when().get("/saga/balance")
                .then()
                .statusCode(200)
                .body(is("30"));
    }

    @Test
    void testPurchaseFailEndpoint() {
        Map<String, Object> purchase = new HashMap<>();
        purchase.put("amount", 101);
        given()
                .contentType(ContentType.JSON)
                .body(purchase)
                .when().post("/saga/purchase")
                .then()
                .statusCode(500);
    }

    @Test
    void testBalanceCompensateEndpoint() {
        given()
                .when().get("/saga/balance")
                .then()
                .statusCode(200)
                .body(is("30"));
    }

}