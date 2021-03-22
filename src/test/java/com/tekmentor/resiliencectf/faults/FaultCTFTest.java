package com.tekmentor.resiliencectf.faults;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

//@SpringBootTest
public class FaultCTFTest {

    WireMockServer wireMockServer;

    @BeforeEach
    public void setup(){
        wireMockServer = new WireMockServer(8092);

        wireMockServer.start();
//        stubFor(any(anyUrl()).willReturn(aResponse().proxiedFrom("http://localhost:8084/")));
        stubFor(
                get("http://localhost:8092/orders/customers/cust-2232")
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withBody("{\"error\": \"Server error\"}")
                                .withHeader("Content-Type", "application/json")
                        )
        );
    }
//http://localhost:8092/orders/customers/{customerId}
    //"http://localhost:8092/orders/customers/cust-2232"
    @Test
    public void customerServiceUnavailable(){

        given()
                .log().all()
                .header("Content-type", "application/json")
                .when()
                .get("http://localhost:8084/customers/{id}/orders","cust-2232")
                .then()
                .log().all()
                .statusCode(200);

    }

    @AfterEach
    public void tearDown(){
        wireMockServer.stop();
    }
}
