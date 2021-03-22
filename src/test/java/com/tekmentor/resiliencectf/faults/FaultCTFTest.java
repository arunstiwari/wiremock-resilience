package com.tekmentor.resiliencectf.faults;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.http.trafficlistener.ConsoleNotifyingWiremockNetworkTrafficListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class FaultCTFTest {

    WireMockServer wireMockServer;


    @BeforeEach
    public void setup(){

        WireMockConfiguration wireMockConfiguration = wireMockConfig()
                .networkTrafficListener(new ConsoleNotifyingWiremockNetworkTrafficListener())
                .port(8092);
        wireMockServer = new WireMockServer(wireMockConfiguration);
        WireMock.configureFor("localhost", 8092);

        wireMockServer.start();

    }

    @Test
    public void orderServiceRespondingWithEmptyResponse(){
        stubFor(
                get(urlEqualTo("/orders/customers/cust-2232"))
                        .willReturn(aResponse()
                                .withFault(Fault.EMPTY_RESPONSE)
                                .withHeader("Content-Type", "application/json")
                        )
        );

        executeRestfulEndpointForDependentOrderService();
    }



    @Test
    public void orderServiceNotAvailable(){
        stubFor(
                get(urlEqualTo("/orders/customers/cust-2232"))
                        .willReturn(serviceUnavailable()
                                .withHeader("Content-Type", "application/json")
                        )
        );

        executeRestfulEndpointForDependentOrderService();
    }

    @Test
    public void callingOrderServiceThrowsServerError(){
        stubFor(
                get(urlEqualTo("/orders/customers/cust-2232"))
                        .willReturn(serverError()
                                .withHeader("Content-Type", "application/json")
                        )
        );

        executeRestfulEndpointForDependentOrderService();
    }

    @Test
    public void orderServiceRespondingWithMalformedResponse(){
        stubFor(
                get(urlEqualTo("/orders/customers/cust-2232"))
                        .willReturn(aResponse()
                                .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                                .withHeader("Content-Type", "application/json")
                        )
        );

        executeRestfulEndpointForDependentOrderService();
    }

    @Test
    public void orderServiceRespondingWithConnectionReset(){
        stubFor(
                get(urlEqualTo("/orders/customers/cust-2232"))
                        .willReturn(aResponse()
                                .withFault(Fault.CONNECTION_RESET_BY_PEER)
                                .withHeader("Content-Type", "application/json")
                        )
        );

        executeRestfulEndpointForDependentOrderService();
    }

    @Test
    public void orderServiceRespondingWithRandomDataClose(){
        stubFor(
                get(urlEqualTo("/orders/customers/cust-2232"))
                        .willReturn(aResponse()
                                .withFault(Fault.RANDOM_DATA_THEN_CLOSE)
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
