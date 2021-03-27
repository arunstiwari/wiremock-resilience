package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServiceUnavailableScenario extends FaultScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceUnavailableScenario.class);
    private final ResilienceReport resilienceReport;

    public ServiceUnavailableScenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter, reportPublisher);
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario(WireMockServer wireMockServer) {
        LOG.info("Execution of serviceUnavailability scenario started");
        this.resilienceReport.setScenarioName("ServiceUnavailabilityScenario");
        ResponseDefinitionBuilder responseWithHeader = serviceUnavailable();
        constructScenarios(responseWithHeader, resilienceReport, wireMockServer);
        LOG.info("Execution of serviceUnavailability scenario ended");
    }
}
