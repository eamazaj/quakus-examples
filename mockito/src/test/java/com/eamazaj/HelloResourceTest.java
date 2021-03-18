package com.eamazaj;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloResourceTest {

    @InjectMock
    HelloService helloService;

    @BeforeAll
    public static void setup() {

    }

    @Test
    public void testHelloEndpoint() {
        Mockito.when(helloService.sayHello()).thenReturn("Hello RESTEasy");
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello RESTEasy"));
    }

    @Test
    public void testByeService() {
        Mockito.when(helloService.sayBye()).thenReturn("Bye Bye");
        Assertions.assertEquals("Bye Bye", helloService.sayBye());
    }
}