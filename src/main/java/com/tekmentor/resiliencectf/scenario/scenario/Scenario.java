package com.tekmentor.resiliencectf.scenario.scenario;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.scenario.builder.IScenarioBuilder;
import com.tekmentor.resiliencectf.scenario.builder.ScenarioBuilderFactory;
import com.tekmentor.resiliencectf.scenario.model.ResilienceResult;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import java.util.List;

public class Scenario {

    private final ResilienceConfiguration configuration;
    private final AvailableScenarios scn;
    private final CTFWireMock wireMock;
    private IReportPublisher publisher;

    public Scenario(ResilienceConfiguration configuration, IReportPublisher reportPublisher, AvailableScenarios scn, CTFWireMock wireMock) {
        this.configuration = configuration;
        this.publisher = reportPublisher;
        this.scn = scn;
        this.wireMock = wireMock;
    }

    public List<ResilienceResult> execute(){
        return buildScenario();
    }

    private List<ResilienceResult> buildScenario() {
        IScenarioBuilder builder = ScenarioBuilderFactory.scenarioBuilder(configuration,scn);
        List<ResilienceResult> results = builder.createScenario(configuration, this.scn, this.wireMock);
        sendReport(results);
        return results;
    }
    private void sendReport(List<ResilienceResult> results){
        publisher.sendReport(results);
    }
}
