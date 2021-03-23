package com.tekmentor.resiliencectf.faults;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.FaultResponseTransformer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class FaultCTFTest {

    public static final String SHIPPING_STATUS_FOR_GIVEN_ORDER_ID = "/shippings/8c6999bd-74ae-440c-9763-be51be157786";
    public static final String ORDERS_CUSTOMERS_CUST_2232 = "/orders/customers/cust-2232";
    WireMockServer wireMockServer;
    WireMockConfiguration wireMockConfiguration;
    Map<String, Object> parameters = new HashMap<>();
/**
 *  dependency-mapping.json
 *  "serverUrl": "http://localhost:8084",
 *  "dependencies": [
 *    {
 *        "url": "http://localhost:8092"
 *        "context": "/orders/customers/cust-2232"
 *    },
 *    {
 *        "url": "http://localhost:8093"
 *        "context": "/shippings/89504b75-9071-4948-aa89-a3194affa335"
 *    }
 *  ]
 *
 *
 *
 *  java -Dresiliency.mapping.path=C:/User/dependency-mapping.json -jar ctf-resiliency.jar
 *
 * */
    @BeforeEach
    public void setup(){

        parameters.put("isLatencyRequired", false);
        parameters.put("latencyPeriod", 1000 );
         wireMockConfiguration = wireMockConfig()
//                .networkTrafficListener(new ConsoleNotifyingWiremockNetworkTrafficListener())
                .extensions(FaultResponseTransformer.class)
                .port(8092);

        wireMockServer = new WireMockServer(wireMockConfiguration);

        WireMock.configureFor("localhost", 8092);

        wireMockServer.start();

    }

    /**
     *  Scenario 1:
     *      Order service respond with Empty Response
     *  Expectation:
     *      If customer service has handled the empty response from Order service, we would get the final response with status 200
     */

    @Test
    public void orderServiceRespondingWithEmptyResponse(){

        stubForFaultResponse(ORDERS_CUSTOMERS_CUST_2232,Fault.EMPTY_RESPONSE);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("isLatencyRequired", false);
        parameters.put("latencyPeriod", 1000 );
        stubForServiceResponseWithValidData(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID, "shipping.json", parameters);

        executeRestfulEndpointForDependentOrderService();
    }


    /**
     *  Scenario 2:
     *      Order service respond with valid Order
     *      ShippingService fails to provide valid reponse. It responds with an empty response
     *  Expectation:
     *      If customer service has handled the empty response from Shipping service, we would get the final response with status 200
     */

    @Test
    public void shippingServiceRespondingWithEmptyResponse(){
        String customersCust2232 = ORDERS_CUSTOMERS_CUST_2232;

        stubForServiceResponseWithValidData(customersCust2232, "order.json", parameters);
        stubForFaultResponse(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID, Fault.EMPTY_RESPONSE);

        executeRestfulEndpointForDependentOrderService();
    }


    /**
     *  Scenario 3:
     *      Order service respond with ServiceNotAvailable error
     *
     *  Expectation:
     *      If customer service has handled the ServiceUnavailability error response from Order service, we would get the final response with status 200
     */

    @Test
    public void orderServiceNotAvailable(){
        UrlPattern urlPattern = urlEqualTo(ORDERS_CUSTOMERS_CUST_2232);

        ResponseDefinitionBuilder responseWithHeader = serviceUnavailable()
                .withHeader("Content-Type", "application/json");

        getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

        UrlPattern urlPattern1 = urlEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID);

        String fileName = "shipping.json";
        int status = 200;

        ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader = aResponse()
                .withStatus(status)
                .withBodyFile(fileName)
                .withHeader("Content-Type", "application/json");
        getStubForGivenStatusAndBodyWithHeader(urlPattern1, responseBuilderWithStatusAndBodyAndHeader);

        executeRestfulEndpointForDependentOrderService();
    }

    private void getStubForGivenStatusAndBodyWithHeader(UrlPattern urlPattern1, ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader) {
        stubFor(
                get(urlPattern1)
                        .willReturn(responseBuilderWithStatusAndBodyAndHeader
                        )
        );
    }

    /**
     *  Scenario 4:
     *      Order service respond with valid order data
     *      Shipping service respond with service unavailability error
     *
     *  Expectation:
     *      If customer service has handled the ServiceUnavailability error response from Shipping service, we would get the final response with status 200
     */

    @Test
    public void shippingServiceNotAvailable(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withStatus(200)
                .withBodyFile("order.json")
                .withHeader("Content-Type", "application/json"));
        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), serviceUnavailable()
                .withHeader("Content-Type", "application/json"));

        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 5:
     *      Order service respond with Server Error
     *
     *  Expectation:
     *      If customer service has handled the ServiceError error response from Order service, we would get the final response with status 200
     */

    @Test
    public void callingOrderServiceThrowsServerError(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), serverError()
                .withHeader("Content-Type", "application/json"));
        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), aResponse()
                .withStatus(200)
                .withBodyFile("shipping.json")
                .withHeader("Content-Type", "application/json"));

        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 6:
     *      Order service respond with valid order
     *      Shipping service respond with Server Error
     *
     *  Expectation:
     *      If customer service has handled the ServiceError error response from Shipping service, we would get the final response with status 200
     */

    @Test
    public void shippingServiceThrowsServerError(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withStatus(200)
                .withBodyFile("order.json")
                .withHeader("Content-Type", "application/json"));
        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), serverError()
                .withHeader("Content-Type", "application/json"));

        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 7:
     *      Order service respond with Malformed response
     *
     *  Expectation:
     *      If customer service has handled the Malformed error response from Order service, we would get the final response with status 200
     */

    @Test
    public void orderServiceRespondingWithMalformedResponse(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                .withHeader("Content-Type", "application/json"));

        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), aResponse()
                .withStatus(200)
                .withBodyFile("shipping.json")
                .withHeader("Content-Type", "application/json"));

        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 8:
     *      Order service respond with valid order
     *      Shipping Service respond with malformed response
     *
     *  Expectation:
     *      If customer service has handled the Malformed error response from Shipping service, we would get the final response with status 200
     */

    @Test
    public void shippingServiceRespondingWithMalformedResponse(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withStatus(200)
                .withBodyFile("order.json")
                .withHeader("Content-Type", "application/json"));

        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), aResponse()
                .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                .withHeader("Content-Type", "application/json"));

        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 9:
     *      Order service respond with Connection reset error
     *
     *  Expectation:
     *      If customer service has handled the Connection reset error response from Order service, we would get the final response with status 200
     */

    @Test
    public void orderServiceRespondingWithConnectionReset(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withFault(Fault.CONNECTION_RESET_BY_PEER)
                .withHeader("Content-Type", "application/json"));
        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), aResponse()
                .withStatus(200)
                .withBodyFile("shipping.json")
                .withHeader("Content-Type", "application/json"));


        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 10:
     *      Order service respond with valid order
     *      Shipping Service respond with error Connection reset
     *
     *  Expectation:
     *      If customer service has handled the Connection reset error response from Shipping service, we would get the final response with status 200
     */
    @Test
    public void shippingServiceRespondingWithConnectionReset(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withStatus(200)
                .withBodyFile("order.json")
                .withHeader("Content-Type", "application/json"));
        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), aResponse()
                .withFault(Fault.CONNECTION_RESET_BY_PEER)
                .withHeader("Content-Type", "application/json"));


        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 11:
     *      Order service respond with error Random data close
     *
     *  Expectation:
     *      If customer service has handled the Random data close error response from Order service, we would get the final response with status 200
     */

    @Test
    public void orderServiceRespondingWithRandomDataClose(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withFault(Fault.RANDOM_DATA_THEN_CLOSE)
                .withHeader("Content-Type", "application/json"));

        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), aResponse()
                .withStatus(200)
                .withBodyFile("shipping.json")
                .withHeader("Content-Type", "application/json"));


        executeRestfulEndpointForDependentOrderService();
    }

    /**
     *  Scenario 12:
     *      Order service respond with valid order
     *      Shipping service respond with error Random data close
     *
     *  Expectation:
     *      If customer service has handled the Random data close error response from Shipping service, we would get the final response with status 200
     */

    @Test
    public void shippingServiceRespondingWithRandomDataClose(){
        getStubForGivenStatusAndBodyWithHeader(urlEqualTo(ORDERS_CUSTOMERS_CUST_2232), aResponse()
                .withStatus(200)
                .withBodyFile("order.json")
                .withHeader("Content-Type", "application/json"));
        getStubForGivenStatusAndBodyWithHeader(urlPathEqualTo(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID), aResponse()
                .withFault(Fault.RANDOM_DATA_THEN_CLOSE)
                .withHeader("Content-Type", "application/json"));


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

    private void stubForFaultResponse(String serviceUrl, Fault fault) {
        stubFor(
                get(urlEqualTo(serviceUrl))
                        .willReturn(aResponse()
                                .withFault(fault)
                                .withHeader("Content-Type", "application/json")
                        )
        );
    }

    private void stubForServiceResponseWithValidData(String serviceUrl, String fileName, Map<String, Object> parameters) {
        stubFor(
                get(urlEqualTo(serviceUrl))
                        .willReturn(aResponse()
                                .withTransformerParameters(parameters)
                                .withBodyFile(fileName)
                                .withHeader("Content-Type", "application/json")
                        )
        );
    }


    @AfterEach
    public void tearDown(){
        wireMockServer.stop();
    }
}
