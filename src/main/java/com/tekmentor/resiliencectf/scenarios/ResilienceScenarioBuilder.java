package com.tekmentor.resiliencectf.scenarios;

import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.scenarios.faults.*;
import com.tekmentor.resiliencectf.scenarios.latency.TimeLatencyScenario;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;

import java.util.ArrayList;

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
        this.scenarios.registerScenario(new EmptyResponseScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new ServiceUnavailableScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new ServerErrorScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new MalformedResponseScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new ConnectionResetScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new RandomDataCloseScenario(this.requestParameter, this.reportPublisher ));

        this.scenarios.registerScenario(new TimeLatencyScenario(this.requestParameter,this.reportPublisher));
        return this.scenarios;
    }


    public Scenarios withOnlyFaultScenarios() {
        this.scenarios.registerScenario(new EmptyResponseScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new ServiceUnavailableScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new ServerErrorScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new MalformedResponseScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new ConnectionResetScenario(this.requestParameter, this.reportPublisher));
        this.scenarios.registerScenario(new RandomDataCloseScenario(this.requestParameter, this.reportPublisher ));

        return this.scenarios;
    }

    public Scenarios withOnlyLatencyScenarios() {
        this.scenarios.registerScenario(new TimeLatencyScenario(this.requestParameter,this.reportPublisher));
        return this.scenarios;
    }

//    public Scenario createScenarios() {
//        Scenario scenarios = new Scenario(this.requestParameter, this.reportPublisher);
//
////        Scenarios scenarios = new FaultScenarios(this.requestParameter, this.reportPublisher);
//        return scenarios;
//    }

}