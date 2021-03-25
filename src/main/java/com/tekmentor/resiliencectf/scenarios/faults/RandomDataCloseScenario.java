package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class RandomDataCloseScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(RandomDataCloseScenario.class);
    private final ResilienceReport resilienceReport;

    public RandomDataCloseScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        super(dependencyUrls, apiUrl, requestType, requestBody,reportPublisher );
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of RandomDataClose Scenario  started");
        this.resilienceReport.setScenarioName("RandomDataClose Scenario");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER);
        constructScenarios(responseWithHeader, resilienceReport);
        LOG.info("Execution of RandomDataClose Scenario finished");
    }
}
