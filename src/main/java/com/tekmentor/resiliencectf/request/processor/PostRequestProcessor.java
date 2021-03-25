package com.tekmentor.resiliencectf.request.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class PostRequestProcessor implements IRequestProcessor {
    Logger LOG = LoggerFactory.getLogger(PostRequestProcessor.class);

    @Override
    public void process(String apiUrl, String requestBody) {
        LOG.info("apiUrl = {} , body= {}", apiUrl, requestBody);
        try {
                int statusCode = given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .body(requestBody)
                    .when()
                    .post(apiUrl)
                    .then().extract().response().statusCode();

            LOG.info("statusCode = {} " , statusCode);

        }catch (Exception e){
            LOG.error("Error Executing scenarios {}",e);
        }
    }
}
