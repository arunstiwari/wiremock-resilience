package com.tekmentor.resiliencectf.scenarios.latency;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.LatencyScenarios;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TimeLatencyScenario extends LatencyScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(TimeLatencyScenario.class);
    private final ResilienceReport resilienceReport;

    public TimeLatencyScenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter, reportPublisher);
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario(WireMockServer wireMockServer) {
        LOG.info("Execution of TimeLatencyScenario  started");
        this.resilienceReport.setScenarioName("TimeLatencyScenario");

        ResponseDefinitionBuilder responseWithHeader = aResponse();
        constructScenarios(responseWithHeader,this.resilienceReport, wireMockServer);
        LOG.info("Execution of ConnectionReset scenario finished");
    }
}
