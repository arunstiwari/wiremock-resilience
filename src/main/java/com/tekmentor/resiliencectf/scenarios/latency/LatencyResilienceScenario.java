package com.tekmentor.resiliencectf.scenarios.latency;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.LatencyScenarios;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class LatencyResilienceScenario extends LatencyScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(LatencyResilienceScenario.class);
    private final ResilienceReport resilienceReport;
    private final AvailableScenarios scenario;

    public LatencyResilienceScenario(RequestParameter requestParameter, IReportPublisher reportPublisher, AvailableScenarios scenario) {
        super(requestParameter, reportPublisher);
        this.resilienceReport = new ResilienceReport();
        this.scenario = scenario;
    }

    @Override
    public void executeScenario(CTFWireMock wireMockServer) {
        LOG.info("Execution of {}  started",this.scenario.getScenarioName());
        this.resilienceReport.setScenarioName(this.scenario.getScenarioName());

        ResponseDefinitionBuilder responseWithHeader = aResponse();
        constructScenarios(responseWithHeader,this.resilienceReport, wireMockServer);
        LOG.info("Execution of {} finished",this.scenario.getScenarioName());
    }
}
