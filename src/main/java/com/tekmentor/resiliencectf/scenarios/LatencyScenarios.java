package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;

public class LatencyScenarios extends Scenarios{

    public LatencyScenarios(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        super(dependencyUrls, apiUrl, requestType, requestBody, reportPublisher);
    }

    @Override
    protected void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report) {

    }

    @Override
    public Scenarios withAllScenarios() {
        return null;
    }
}
