package com.tekmentor.resiliencectf.scenarios;

import com.tekmentor.resiliencectf.report.IReportPublisher;

public class FaultScenariosBuilder {
    private String[] dependencyUrls;
    private String apiUrl;
    private String requestType;
    private String requestBody;
    private IReportPublisher reportPublisher;

    public FaultScenariosBuilder setDependencyUrls(String[] dependencyUrls) {
        this.dependencyUrls = dependencyUrls;
        return this;
    }

    public FaultScenariosBuilder setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public FaultScenariosBuilder setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public FaultScenariosBuilder setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }


    public FaultScenarios createFaultScenarios() {
        FaultScenarios scenarios = new FaultScenarios(dependencyUrls, apiUrl, requestType,requestBody, reportPublisher);
        return scenarios;
    }

    public FaultScenariosBuilder attachReportPublisher(IReportPublisher reportPublisher) {
        this.reportPublisher = reportPublisher;
        return this;
    }
}