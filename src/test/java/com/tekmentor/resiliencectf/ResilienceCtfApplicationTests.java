package com.tekmentor.resiliencectf;


import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResilienceCtfApplicationTests {

    WireMockServer server;

    @BeforeEach
    public void setup(){
        server = new WireMockServer(8081);

        server.start();
//        server.startRecording("http://localhost:8092/");
//        http://localhost:8092/orders/customers/{customerId}
    }

    @Test
    void getAllCustomers() {
        given()
                .log().all()
                .when()
                .get("http://localhost:8081/customers")
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    void createNewCustomer() {
        given()
                .log().all()
                .header("Content-type", "application/json")
                .body("{\"name\": \"Customer5\", \"age\": \"54\"}")
                .when()
                .post("http://localhost:8081/customers")
                .then()
                .statusCode(201);
    }

    @Test
    void fetchOrdersForSpecificCustomer() {
        given()
                .log().all()
                .when()
                .get("http://localhost:8081/orders/customers/{customerId}","cust-2232")
                .then()
                .statusCode(200)
                .log().all();

    }

    @AfterEach
    public void tearDown(){
//        server.stopRecording();
        server.stop();
    }

}
