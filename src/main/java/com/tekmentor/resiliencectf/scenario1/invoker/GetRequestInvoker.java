package com.tekmentor.resiliencectf.scenario1.invoker;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenario1.model.ExecutionResponse;
import com.tekmentor.resiliencectf.scenario1.model.ResilienceResult;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class GetRequestInvoker implements IRequestInvoker {
    private static  final Logger LOG = LoggerFactory.getLogger(GetRequestInvoker.class);

    @Override
    public ResilienceResult invoke(ResilienceConfiguration configuration, AvailableScenarios scn, ResilienceResult result) {
        LOG.info("apiUrl = {} ", configuration.getApiUrl());
        result.setScn(scn.getScenarioName());

        long startTime = System.currentTimeMillis();
        try {
            Response response = given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .header("scn", scn.getScenarioName())
                    .when()
                    .get(configuration.getApiUrl())
                    .then().extract().response();

            int statusCode = response.statusCode();
            LOG.info("response = {} " , response.getBody().prettyPrint());

            ExecutionResponse executionResponse = new ExecutionResponse(response.statusCode(),response.getBody().asString());
            result.setResponse(executionResponse);

        }catch (Exception e){
            LOG.error("Error Executing scenarios {}",e);
            ExecutionResponse executionResponse = new ExecutionResponse(500,e.getMessage());
            result.setResponse(executionResponse);
            result.setException(e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        result.setExecutionTime(endTime - startTime);

        return result;
    }
}
