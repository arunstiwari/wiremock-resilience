package com.tekmentor.resiliencectf.scenarios.model;

public class RequestParameterBuilder {
    private String[] dependencyUrls;
    private String apiUrl;
    private String requestType;
    private String requestBody;
    private int apiLatencyThreshold;
    private int dependentApiLatencyThreshold;

    public RequestParameterBuilder() {
    }


    public RequestParameterBuilder setDependencyUrls(String[] dependencyUrls) {
        this.dependencyUrls = dependencyUrls;
        return this;
    }

    public RequestParameterBuilder setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }
    public RequestParameterBuilder setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }
    public RequestParameterBuilder setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public RequestParameterBuilder setApiLatencyThreshold(int latency) {
        this.apiLatencyThreshold = latency;
        return this;
    }


    public RequestParameterBuilder setDependentApiThreshold(int latency) {
        this.dependentApiLatencyThreshold = latency;
        return this;
    }

    public RequestParameter createRequestParameter() {
        return new RequestParameter(dependencyUrls,apiUrl,requestType,requestBody,apiLatencyThreshold,dependentApiLatencyThreshold);
    }
}
