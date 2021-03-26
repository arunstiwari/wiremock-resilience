package com.tekmentor.resiliencectf.scenarios;

import com.tekmentor.resiliencectf.report.IReportPublisher;

public class ResilienceScenarioBuilder {
    private String[] dependencyUrls;
    private String apiUrl;
    private String requestType;
    private String requestBody;
    private IReportPublisher reportPublisher;

    public ResilienceScenarioBuilder setDependencyUrls(String[] dependencyUrls) {
        this.dependencyUrls = dependencyUrls;
        return this;
    }

    public ResilienceScenarioBuilder setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public ResilienceScenarioBuilder setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public ResilienceScenarioBuilder setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }


    public Scenarios createFaultScenarios() {
        Scenarios scenarios = new FaultScenarios(dependencyUrls, apiUrl, requestType,requestBody, reportPublisher);
        return scenarios;
    }

    public ResilienceScenarioBuilder attachReportPublisher(IReportPublisher reportPublisher) {
        this.reportPublisher = reportPublisher;
        return this;
    }
}