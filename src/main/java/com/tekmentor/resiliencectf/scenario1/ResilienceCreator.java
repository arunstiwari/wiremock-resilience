package com.tekmentor.resiliencectf.scenario1;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.scenario1.scenario.Scenario;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import java.util.List;

public class ResilienceCreator {

    private final ResilienceConfiguration configuration;
    private final IReportPublisher reportPublisher;
    private  Scenarios scenarios;
    private final CTFWireMock wireMock;

    public ResilienceCreator(ResilienceConfiguration configuration, IReportPublisher reportPublisher, CTFWireMock wireMock) {
        this.configuration = configuration;
        this.reportPublisher = reportPublisher;
        this.scenarios = new Scenarios();
        this.wireMock = wireMock;
    }

    public Scenarios simulateAllFaultScenarios() {
        List<AvailableScenarios> faultsScenarios = AvailableScenarios.getAllFaultsScenarios();
        for (AvailableScenarios scn : faultsScenarios){
            this.scenarios.registerScenario(new Scenario(configuration, reportPublisher,scn,wireMock));
        }

        return this.scenarios;
    }


    public Scenarios simulateAllLoadLatencyScenarios() {
        this.scenarios = new Scenarios();
        List<AvailableScenarios> latencyScenarios = AvailableScenarios.getAllLatencyScenarios();
        for (AvailableScenarios scn : latencyScenarios){
            this.scenarios.registerScenario(new Scenario(configuration, reportPublisher,scn,wireMock));
        }
        return this.scenarios;
    }

    public Scenarios simulate10sLatencyScenarios() {
        this.scenarios = new Scenarios();
        this.scenarios.registerScenario(new Scenario(configuration, reportPublisher,AvailableScenarios.TimeLatencyWith10SecondsAnd5RequestsPerSecond,wireMock));
        return this.scenarios;
    }

    public Scenarios simulate30sLatencyScenarios() {
        this.scenarios = new Scenarios();
        this.scenarios.registerScenario(new Scenario(configuration, reportPublisher,AvailableScenarios.TimeLatencyWith30SecondsAnd5RequestsPerSecond,wireMock));
        return this.scenarios;
    }
}
