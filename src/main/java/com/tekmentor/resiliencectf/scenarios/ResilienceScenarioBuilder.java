package com.tekmentor.resiliencectf.scenarios;

import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.scenarios.execution.faults.FaultResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.execution.latency.LatencyResilienceScenario;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenarios.execution.latency.LoadLatencyResilienceScenario;
import com.tekmentor.resiliencectf.util.AvailableScenarios;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ResilienceScenarioBuilder {
    private ResilienceConfiguration requestParameter;
    private IReportPublisher reportPublisher;
    private ResilienceScenarios scenarios;

    ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(2);
//    ScheduledExecutorService executor2 = Executors.newSingleThreadScheduledExecutor();

    public ResilienceScenarioBuilder(ResilienceScenarios scenarios) {
        this.scenarios = scenarios;
    }

    public ResilienceScenarioBuilder setRequestParameter(ResilienceConfiguration requestParameter) {
        this.requestParameter = requestParameter;
        return this;
    }
    public ResilienceScenarioBuilder attachReportPublisher(IReportPublisher reportPublisher) {
        this.reportPublisher = reportPublisher;
        return this;
    }


    public ResilienceScenarios withBothFaultAndLatencyScenarios() {
        List<AvailableScenarios> allLatencyScenarios = AvailableScenarios.getAllLatencyScenarios();
                for (AvailableScenarios scenario : allLatencyScenarios){
            if (scenario.isLoad()) {
                LoadLatencyResilienceScenario loadLatencyResilienceScenario = new LoadLatencyResilienceScenario(this.requestParameter, this.reportPublisher, scenario);
                if (scenario.getScenarioName().equals("TimeLatencyWith10SecondsAnd5RequestsPerSecond")){
                    loadLatencyResilienceScenario.setExecutor(this.executor1,10);
                }else {
                    loadLatencyResilienceScenario.setExecutor(this.executor1,30);
                }

                this.scenarios.registerScenario(loadLatencyResilienceScenario);
            }else {
                this.scenarios.registerScenario(new LatencyResilienceScenario(this.requestParameter, this.reportPublisher, scenario));
            }
        }

        List<AvailableScenarios> allFaultsScenarios = AvailableScenarios.getAllFaultsScenarios();
        for (AvailableScenarios fault : allFaultsScenarios){
            this.scenarios.registerScenario(new FaultResilienceScenario(this.requestParameter, this.reportPublisher,fault));
        }
        return this.scenarios;
    }


    public ResilienceScenarios withOnlyFaultScenarios() {
        List<AvailableScenarios> allFaultsScenarios = AvailableScenarios.getAllFaultsScenarios();
        for (AvailableScenarios fault : allFaultsScenarios){
            this.scenarios.registerScenario(new FaultResilienceScenario(this.requestParameter, this.reportPublisher,fault));
        }
        return this.scenarios;
    }

    public ResilienceScenarios withOnlyLatencyScenarios() {
        List<AvailableScenarios> allLatencyScenarios = AvailableScenarios.getAllLatencyScenarios();
        for (AvailableScenarios scenario : allLatencyScenarios){
            if (scenario.isLoad()) {
                LoadLatencyResilienceScenario loadLatencyResilienceScenario = new LoadLatencyResilienceScenario(this.requestParameter, this.reportPublisher, scenario);
                if (scenario.getScenarioName().equals("TimeLatencyWith10SecondsAnd5RequestsPerSecond")){
                    loadLatencyResilienceScenario.setExecutor(this.executor1,10000);
                }else {
                    loadLatencyResilienceScenario.setExecutor(this.executor1,30000);
                }
                this.scenarios.registerScenario(loadLatencyResilienceScenario);
            }else {
                this.scenarios.registerScenario(new LatencyResilienceScenario(this.requestParameter, this.reportPublisher, scenario));
            }
        }
        return this.scenarios;
    }
}