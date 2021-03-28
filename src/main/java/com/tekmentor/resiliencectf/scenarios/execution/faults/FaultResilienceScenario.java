package com.tekmentor.resiliencectf.scenarios.execution.faults;

import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.execution.IResilienceScenario;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaultResilienceScenario extends FaultScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(FaultResilienceScenario.class);
    private final ResilienceReport resilienceReport;
    private AvailableScenarios scenario;

    public FaultResilienceScenario(ResilienceConfiguration requestParameter, IReportPublisher reportPublisher, AvailableScenarios scenario) {
        super(requestParameter,reportPublisher );
        this.resilienceReport = new ResilienceReport();
        this.scenario = scenario;
    }

    @Override
    public void executeScenario(CTFWireMock wireMockServer) {
        LOG.info("Execution of {}  scenario started",this.scenario.getScenarioName());
        this.resilienceReport.setScenarioName(this.scenario.getScenarioName());
        constructScenarios(this.scenario.getResponseBuilder(),this.resilienceReport, wireMockServer);
        LOG.info("Execution of {} finished", this.scenario.getScenarioName());

    }
}
