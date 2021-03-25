package com.tekmentor.resiliencectf.request.processor;

import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class PostRequestProcessor implements IRequestProcessor {
    Logger LOG = LoggerFactory.getLogger(PostRequestProcessor.class);

    @Override
    public ExecutionResult process(String apiUrl, String requestBody) {
        LOG.info("apiUrl = {} , body= {}", apiUrl, requestBody);
        ExecutionResult result = new ExecutionResult();
        try {
                int statusCode = given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .body(requestBody)
                    .when()
                    .post(apiUrl)
                    .then().extract().response().statusCode();

            LOG.info("statusCode = {} " , statusCode);
            result.status(statusCode);
            result.message("Successfull");

        }catch (Exception e){
            LOG.error("Error Executing scenarios {}",e);
            result.status(-1);
            result.message("Failure");
            result.exception(e);
        }
        return result;
    }
}
