package com.tekmentor.resiliencectf.request.processor;

import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class GetRequestProcessor implements IRequestProcessor {
    Logger LOG = LoggerFactory.getLogger(GetRequestProcessor.class);

    @Override
    public ExecutionResult process(String apiUrl, String requestBody) {
        LOG.info("apiUrl = {} , body= {}", apiUrl, requestBody);
        ExecutionResult result = new ExecutionResult();
        try {
            int statusCode = given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .when()
                    .get(apiUrl)
                    .then().extract().response().statusCode();

            LOG.info("statusCode = {} " , statusCode);
           result.setExceptionAndStatus(statusCode);
        }catch (Exception e){
            LOG.error("Error Executing scenarios {}",e);
            result.setExceptionAndStatus(500);
            result.exception(e);
        }

        return result;
    }
}
