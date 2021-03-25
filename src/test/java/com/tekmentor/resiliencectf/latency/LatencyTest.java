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

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class LatencyTest {

    public static final String SHIPPING_STATUS_FOR_GIVEN_ORDER_ID = "/shippings/78c6999bd-74ae-440c-9763-be51be157786";
    public static final String ORDERS_CUSTOMERS_CUST_2232 = "/orders/customers/cust-2232";
    WireMockServer wireMockServer;
    WireMockConfiguration wireMockConfiguration;
    Map<String, Object> parameters = new HashMap<>();

    @BeforeEach
    public void setup(){
        WireMockConfiguration wireMockConfiguration = wireMockConfig()
//                .networkTrafficListener(new ConsoleNotifyingWiremockNetworkTrafficListener())
                .extensions(LatencyResponseTransformer.class)
                .port(8092);
        wireMockServer = new WireMockServer(wireMockConfiguration);
        WireMock.configureFor("localhost", 8092);

        wireMockServer.start();
//        given()
//                .log().all()
//                .header("Content-type", "application/json")
//                .body("{\"milliseconds\": 300}")
//                .when()
//                .post("http://localhost:8092/__admin/socket-delay");
    }

    @Test
    public void simulateOrderServiceDelayWith1000msAndShippingServiceWithNoDelay(){
        parameters.put("serviceUrl", ORDERS_CUSTOMERS_CUST_2232);
        parameters.put("isLatencyRequired", true);
        stubFor(
                get(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232))
                        .willReturn(aResponse()
                                .withTransformerParameters(parameters)
                                .withStatus(200)
                                 .withFixedDelay(5000)
                                .withBodyFile("order.json")
                                .withHeader("Content-Type", "application/json")
                        )
        );

        parameters.put("serviceUrl", SHIPPING_STATUS_FOR_GIVEN_ORDER_ID);
        parameters.put("isLatencyRequired", false);
        stubFor(
                get(urlEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID))
                        .willReturn(aResponse()
                                .withTransformerParameters(parameters)
                                .withStatus(200)
                                .withFixedDelay(50000)
                                .withBodyFile("shipping.json")
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
