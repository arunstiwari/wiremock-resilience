package com.tekmentor.resiliencectf.scenario.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Result structure
 *          *  [{
 *          *      "scn": "ConnectionResetScenario",
 *          *      "executionTime": 40,
 *          *      "status": "SUCCESS",
 *                  "dependency": {
 *                    "context": "/orders/customers/cust-223"
 *                  },
 *          *      "response": {
 *          *        "statusCode": 200,
 *          *        "body": ""
 *          *      },
 *          *      "exception": ""
 *          *  }]
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResilienceResult {
    private String scn;
    private long executionTime;
    private String status;
    private ExecutionResponse response;
    private String exception;
    private Dependency dependency;

    public ResilienceResult() {
    }

    public ResilienceResult(String scn, int executionTime, String status, ExecutionResponse response, String exception) {
        this.scn = scn;
        this.executionTime = executionTime;
        this.status = status;
        this.response = response;
        this.exception = exception;
    }


    public String getScn() {
        return scn;
    }

    public void setScn(String scn) {
        this.scn = scn;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ExecutionResponse getResponse() {
        return response;
    }

    public void setResponse(ExecutionResponse response) {
        this.response = response;
        if (response.getStatusCode() == 500){
            this.status = "FAILED";
        }else{
            this.status = "SUCCESS";
        }
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("scn", scn)
                .append("executionTime", executionTime)
                .append("status", status)
                .append("response", response)
                .append("exception", exception)
                .append("dependency", dependency)
                .toString();
    }
}
