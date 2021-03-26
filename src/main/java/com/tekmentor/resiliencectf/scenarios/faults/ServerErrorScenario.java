package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServerErrorScenario extends FaultScenarios implements IResilienceScenario {
    private static final Logger LOG = LoggerFactory.getLogger(ServerErrorScenario.class);
    private final ResilienceReport resilienceReport;

    public ServerErrorScenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter,reportPublisher );
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of serverError scenario started");
        this.resilienceReport.setScenarioName("ServerErrorsScenario");
        ResponseDefinitionBuilder responseWithHeader = serverError();

        constructScenarios(responseWithHeader, resilienceReport);
        LOG.info("Execution of serverError scenario finished");
    }
}
