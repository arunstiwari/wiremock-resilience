package com.tekmentor.resiliencectf.request.processor;

import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import io.restassured.response.Response;
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
            Response response  = given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .body(requestBody)
                    .when()
                    .post(apiUrl)
                    .then().extract().response();


            int statusCode = response.statusCode();

            LOG.info("statusCode = {} " , statusCode);
            result.setExceptionAndStatus(response.statusCode(),response.getBody().asString());
            if(result.getStatus() == -1){
                LOG.error("Exception : {}",response.getBody().prettyPrint());
                throw result.getException();
            }
        }catch (Exception e){
            LOG.error("Error Executing scenarios {}",e);
            result.setExceptionAndStatus(500,e.getMessage());
            result.exception(e);
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}
