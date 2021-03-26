package com.tekmentor.resiliencectf.scenarios.latency;

import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.LatencyScenarios;

public class TimeLatencyScenario extends LatencyScenarios implements IResilienceScenario {

    public TimeLatencyScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        super(dependencyUrls, apiUrl, requestType, requestBody, reportPublisher);
    }

    @Override
    public void executeScenario() {
        //TODO latency scenario implementation
    }
}
