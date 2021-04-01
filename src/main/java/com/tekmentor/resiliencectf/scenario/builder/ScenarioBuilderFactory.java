package com.tekmentor.resiliencectf.scenario.builder;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.util.AvailableScenarios;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScenarioBuilderFactory {


    public static IScenarioBuilder scenarioBuilder(ResilienceConfiguration configuration, AvailableScenarios scenarios){

       if (scenarios.isLoad()){
           ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
           return new LoadLatencyScenarioBuilder(executor);
        } else if (scenarios.isLatencyScenario()){
           return new LatencyScenarioBuilder();
       }else
           return new FaultScenarioBuilder();
    }
}
