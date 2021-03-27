package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class RandomDataCloseScenario extends FaultScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(RandomDataCloseScenario.class);
    private final ResilienceReport resilienceReport;

    public RandomDataCloseScenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter,reportPublisher );
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario(WireMockServer wireMockServer) {
        LOG.info("Execution of RandomDataClose Scenario  started");
        this.resilienceReport.setScenarioName("RandomDataClose Scenario");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER);
        constructScenarios(responseWithHeader, resilienceReport, wireMockServer );
        LOG.info("Execution of RandomDataClose Scenario finished");
    }
}
