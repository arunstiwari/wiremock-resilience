package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServiceUnavailableScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceUnavailableScenario.class);
    private final ResilienceReport resilienceReport;

    public ServiceUnavailableScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        super(dependencyUrls, apiUrl, requestType, requestBody, reportPublisher);
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of serviceUnavailability scenario started");
        this.resilienceReport.setScenarioName("ServiceUnavailabilityScenario");
        ResponseDefinitionBuilder responseWithHeader = serviceUnavailable();
        constructScenarios(responseWithHeader, resilienceReport);
        LOG.info("Execution of serviceUnavailability scenario ended");
    }
}
