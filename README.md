### 1. Resilience Test Scenarios
+ Scenario 1
```java
/**
     *  Scenario 1:
     *      Order service respond with Empty Response
     *  Expectation:
     *      If customer service has handled the empty response from Order service, we would get the final response with status 200
     */

    @Test
    public void orderServiceRespondingWithEmptyResponse(){
        stubForFaultResponse(ORDERS_CUSTOMERS_CUST_2232,Fault.EMPTY_RESPONSE);

        stubForServiceResponseWithValidData(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID, "shipping.json");

        executeRestfulEndpointForDependentOrderService();
    }
```
+ Scenario 2
```java
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

        stubForServiceResponseWithValidData(customersCust2232, "order.json");
        stubForFaultResponse(SHIPPING_STATUS_FOR_GIVEN_ORDER_ID, Fault.EMPTY_RESPONSE);

        executeRestfulEndpointForDependentOrderService();
    }

```
+ Scenario 3
```java
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
```
+ Scenario 4
```java
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

```
+ Scenario 5
```java
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

```

+ Scenario 6
```java
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

```

+ Scenario 7
```java
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

```

+ Scenario 8
```java
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

```

+ Scenario 9
```java
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

```

+ Scenario 10
```java
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

```

+ Scenario 11
```java
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

```

+ Scenario 12
```java
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

```