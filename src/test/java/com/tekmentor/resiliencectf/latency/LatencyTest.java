package com.tekmentor.resiliencectf.latency;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.http.trafficlistener.ConsoleNotifyingWiremockNetworkTrafficListener;
import com.tekmentor.resiliencectf.LatencyResponseTransformer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class LatencyTest {

    WireMockServer wireMockServer;

    @BeforeEach
    public void setup(){
        WireMockConfiguration wireMockConfiguration = wireMockConfig()
                .networkTrafficListener(new ConsoleNotifyingWiremockNetworkTrafficListener())
                .extensions(LatencyResponseTransformer.class)
                .port(8092);
        wireMockServer = new WireMockServer(wireMockConfiguration);
        WireMock.configureFor("localhost", 8092);

        wireMockServer.start();
        given()
                .log().all()
                .header("Content-type", "application/json")
                .body("{\"milliseconds\": 300}")
                .when()
                .post("http://localhost:8092/__admin/socket-delay");
    }

    @Test
    public void socketConnectionTimeout(){
//        stubFor(
//                get(urlEqualTo("/orders/customers/cust-2232"))
//                        .willReturn(aResponse()
//                                .withStatus(200)
//                                 .withFixedDelay(5000)
//                                .withBodyFile("order.json")
//                                .withHeader("Content-Type", "application/json")
//                        )
//        );

        stubFor(
                get(urlEqualTo("/orders/customers/cust-2232"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withFixedDelay(50000)
                                .withBodyFile("order.json")
                                .withHeader("Content-Type", "application/json")
                        )
        );

        executeRestfulEndpointForDependentOrderService();

    }

    private void executeRestfulEndpointForDependentOrderService() {
        given()
                .log().all()
                .header("Content-type", "application/json")
                .when()
                .get("http://localhost:8084/customers/{id}/orders", "cust-2232")
                .then()
                .log().all()
                .statusCode(200);
    }

    @AfterEach
    public void tearDown(){
        wireMockServer.stop();
    }
}
