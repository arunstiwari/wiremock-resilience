package com.tekmentor.resiliencectf.scenarios;

import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.scenarios.faults.*;
import com.tekmentor.resiliencectf.scenarios.latency.LatencyResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;
import com.tekmentor.resiliencectf.util.AvailableScenarios;

import java.util.List;

public class ResilienceScenarioBuilder {
    private RequestParameter requestParameter;
    private IReportPublisher reportPublisher;
    private Scenarios scenarios;

    public ResilienceScenarioBuilder(Scenarios scenarios) {
        this.scenarios = scenarios;
    }

    public ResilienceScenarioBuilder setRequestParameter(RequestParameter requestParameter) {
        this.requestParameter = requestParameter;
        return this;
    }
    public ResilienceScenarioBuilder attachReportPublisher(IReportPublisher reportPublisher) {
        this.reportPublisher = reportPublisher;
        return this;
    }

    public Scenarios withBothFaultAndLatencyScenarios() {
        List<AvailableScenarios> allLatencyScenarios = AvailableScenarios.getAllLatencyScenarios();
        for (AvailableScenarios scenario : allLatencyScenarios){
            this.scenarios.registerScenario(new LatencyResilienceScenario(this.requestParameter,this.reportPublisher, scenario));
        }

        List<AvailableScenarios> allFaultsScenarios = AvailableScenarios.getAllFaultsScenarios();
        for (AvailableScenarios fault : allFaultsScenarios){
            this.scenarios.registerScenario(new FaultResilienceScenario(this.requestParameter, this.reportPublisher,fault));
        }
        return this.scenarios;
    }


    public Scenarios withOnlyFaultScenarios() {
        List<AvailableScenarios> allFaultsScenarios = AvailableScenarios.getAllFaultsScenarios();
        for (AvailableScenarios fault : allFaultsScenarios){
            this.scenarios.registerScenario(new FaultResilienceScenario(this.requestParameter, this.reportPublisher,fault));
        }
        return this.scenarios;
    }

    public Scenarios withOnlyLatencyScenarios() {
        List<AvailableScenarios> allLatencyScenarios = AvailableScenarios.getAllLatencyScenarios();
        for (AvailableScenarios scenario : allLatencyScenarios){
            this.scenarios.registerScenario(new LatencyResilienceScenario(this.requestParameter,this.reportPublisher, scenario));
        }
        return this.scenarios;
    }

}